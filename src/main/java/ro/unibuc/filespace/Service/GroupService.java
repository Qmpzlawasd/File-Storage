package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.filespace.Dto.InvitationDto;
import ro.unibuc.filespace.Exception.GroupAlreadyExists;
import ro.unibuc.filespace.Exception.GroupDoesNotExist;
import ro.unibuc.filespace.Exception.UserDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Helper.EncryptionHelper;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Repository.GroupRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {
    private final GroupRepository groupRepository;

    private final EncryptionHelper encryptionHelper;

    private final UserService userService;

    private final MembershipService membershipService;

    public void addUserToGroup(User user, Group group) throws GroupDoesNotExist, UserDoesNotExist {
        Group thisGroup = groupRepository.findByGroupId(group.getGroupId()).orElseThrow(GroupDoesNotExist::new);
        User thisUser = userService.findUserById(user.getUserId()).orElseThrow(UserDoesNotExist::new);

        if (groupRepository.findUserInGroup(group.getGroupId(), user.getUserId()).isPresent()) {
            log.info("User with id {} is already in group with id {}", user.getUserId(), group.getGroupId());
            return;
        }
        membershipService.createMembership(thisGroup, thisUser);
    }

    public Optional<Group> getGroup(long id) throws GroupDoesNotExist {
        return groupRepository.findByGroupId(id);
    }

    public Optional<Group> getGroup(String groupName) throws GroupDoesNotExist {
        return groupRepository.findByGroupName(groupName);
    }

    public Group createGroup(String groupName, User user) throws GroupAlreadyExists {
        if (groupRepository.findByGroupName(groupName).isPresent()) {
            throw new GroupAlreadyExists();
        }
        Group newGroup = groupRepository.save(new Group(groupName));
        log.info("Created group in seeder with name {} and internal id {}", groupName, newGroup.getGroupId());
        this.addUserToGroup(user, newGroup);
        return newGroup;
    }

    public Group createGroup(String groupName) throws GroupAlreadyExists {
        if (groupRepository.findByGroupName(groupName).isPresent()) {
            throw new GroupAlreadyExists();
        }
        Group newGroup = groupRepository.save(new Group(groupName));
        log.info("Created group with name {} and internal id {}", groupName, newGroup.getGroupId());
        this.addUserToGroup(userService.getAuthenticatedUser(), newGroup);
        return newGroup;
    }

    public Optional<User> getUserFromGroup(long groupId, long userId) {
        log.debug("Checking if user ID {} exists in group ID {}", userId, groupId);
        Optional<User> user = groupRepository.findUserInGroup(groupId, userId);
        if (user.isPresent()) {
            log.debug("User ID {} found in group ID {}", userId, groupId);
        } else {
            log.debug("User ID {} not found in group ID {}", userId, groupId);
        }
        return user;
    }

    public List<User> getGroupUsers(long id) throws UserNotInGroup {
        log.info("Fetching users for group ID {}", id);

        long authenticatedUserId = userService.getAuthenticatedUser().getUserId();
        log.debug("Authenticated user ID: {}", authenticatedUserId);

        this.getUserFromGroup(id, authenticatedUserId).orElseThrow(() -> {
            log.warn("User ID {} not authorized to access group ID {}", authenticatedUserId, id);
            return new UserNotInGroup();
        });

        List<User> users = membershipService.getUsersInGroup(id);
        log.info("Found {} users in group ID {}", users.size(), id);
        return users;
    }

    public String generateInvitation(String groupName, String username) throws UserDoesNotExist, GroupDoesNotExist, UserNotInGroup, Exception {
        log.info("Generating invitation for user '{}' to group '{}'", username, groupName);

        User thisUser = userService.findUserByUsername(username).orElseThrow(() -> {
            log.error("User '{}' not found", username);
            return new UserDoesNotExist();
        });

        Group thisGroup = this.getGroup(groupName).orElseThrow(() -> {
            log.error("Group '{}' not found", groupName);
            return new GroupDoesNotExist();
        });

        long authenticatedUserId = userService.getAuthenticatedUser().getUserId();
        this.getUserFromGroup(thisGroup.getGroupId(), authenticatedUserId).orElseThrow(() -> {
            log.warn("User ID {} not authorized to invite to group '{}'", authenticatedUserId, groupName);
            return new UserNotInGroup();
        });

        String token = encryptionHelper.encodeInvitation(groupName, username);
        log.debug("Generated invitation token for user '{}' to group '{}'", username, groupName);
        return token;
    }

    public void acceptInvitation(String token) throws Exception {
        log.info("Processing invitation acceptance for token: {}", token);

        InvitationDto receivedInvite = encryptionHelper.decodeInvitation(token);
        log.debug("Decoded invitation: {}", receivedInvite);

        String currentUsername = userService.getAuthenticatedUser().getUsername();
        if (!receivedInvite.getUsername().equals(currentUsername)) {
            log.error("Invite username '{}' doesn't match current user '{}'",
                    receivedInvite.getUsername(), currentUsername);
            throw new RuntimeException("Invite does not match this user");
        }

        Group invitedGroup = getGroup(receivedInvite.getGroupName()).orElseThrow(() -> {
            log.error("Group '{}' from invitation not found", receivedInvite.getGroupName());
            return new GroupDoesNotExist();
        });

        User currentUser = userService.getAuthenticatedUser();
        log.info("Adding user '{}' to group '{}' via invitation",
                currentUser.getUsername(), invitedGroup.getGroupName());

        addUserToGroup(currentUser, invitedGroup);
        log.info("Successfully added user '{}' to group '{}'",
                currentUser.getUsername(), invitedGroup.getGroupName());
    }
}

