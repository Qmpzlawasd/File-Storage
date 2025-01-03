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
        return groupRepository.findUserInGroup(groupId, userId);
    }

    public List<User> getGroupUsers(long id) throws UserNotInGroup {
        this.getUserFromGroup(id, userService.getAuthenticatedUser().getUserId()).orElseThrow(UserNotInGroup::new);
        return membershipService.getUsersInGroup(id);
    }

    public String generateInvitation(String groupName, String username) throws UserDoesNotExist, GroupDoesNotExist, UserNotInGroup, Exception {
        User thisUser = userService.findUserByUsername(username).orElseThrow(UserDoesNotExist::new);
        Group thisGroup = this.getGroup(groupName).orElseThrow(GroupDoesNotExist::new);
        this.getUserFromGroup(thisGroup.getGroupId(), userService.getAuthenticatedUser().getUserId()).orElseThrow(UserNotInGroup::new);
        return encryptionHelper.encodeInvitation(groupName, username);
    }

    public void acceptInvitation(String token) throws Exception {
        InvitationDto receivedInvite = encryptionHelper.decodeInvitation(token);
        if (!receivedInvite.getUsername().equals(userService.getAuthenticatedUser().getUsername())) {
            throw new RuntimeException("Invite does not match this user");
        }
        Group invitedGroup = getGroup(receivedInvite.getGroupName()).orElseThrow(GroupDoesNotExist::new);
        addUserToGroup(userService.getAuthenticatedUser(), invitedGroup);
    }
}

