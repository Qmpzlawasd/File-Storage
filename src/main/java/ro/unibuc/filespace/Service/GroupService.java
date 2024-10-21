package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.filespace.Exception.GroupAlreadyExists;
import ro.unibuc.filespace.Exception.GroupDoesNotExist;
import ro.unibuc.filespace.Exception.UserDoesNotExist;
import ro.unibuc.filespace.Model.File;
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

    private final UserService userService;

    private final MembershipService membershipService;

    public void addUserToGroup(long userId, long groupId) throws GroupDoesNotExist , UserDoesNotExist{
        Group thisGroup = groupRepository.findByGroupId(groupId).orElseThrow(GroupDoesNotExist::new);
        User thisUser = userService.findUserById(userId).orElseThrow(UserDoesNotExist::new);

        if (groupRepository.findUserInGroup(groupId, userId).isPresent()) {
            log.info("User with id {} is already in group with id {}", userId, groupId);
            return;
        }
        membershipService.createMembership(thisGroup, thisUser);
    }

    public Group getGroup(long id) throws GroupDoesNotExist {
        return groupRepository.findByGroupId(id).orElseThrow(GroupDoesNotExist::new);
    }

    public void createGroup(String groupName) throws GroupAlreadyExists {
        if (groupRepository.findByGroupName(groupName).isPresent()) {
            throw new GroupAlreadyExists();
        }
        Group newGroup = groupRepository.save(new Group(groupName));
        log.info("Created group with name {} and internal id {}", groupName, newGroup.getGroupId());
        this.addUserToGroup(userService.getAuthenticatedUser().getUserId(), newGroup.getGroupId());
    }

    public List<User> getGroupUsers(long id) throws GroupDoesNotExist {
        return groupRepository.getUsersInGroup(id);
    }

    public Optional<User> getUserFromGroup(long groupId, long userId) throws GroupDoesNotExist {
        return groupRepository.findUserInGroup(groupId, userId);
    }
}

