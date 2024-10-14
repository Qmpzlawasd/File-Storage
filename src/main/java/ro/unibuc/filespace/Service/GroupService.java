package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.filespace.Exception.GroupDoesNotExist;
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

    public Group getGroup(long id) throws GroupDoesNotExist {
        return groupRepository.findByGroupId(id).orElseThrow(GroupDoesNotExist::new);
    }

    public void createGroup(String groupName)  {
        Group asd = groupRepository.save(new Group(groupName));
        log.info("Created group with name {}", groupName);

    }

    public List<User> getGroupUsers(long id) throws GroupDoesNotExist {
        return groupRepository.getUsersInGroup(id);
    }

    public Optional<User> getUserFromGroup(long groupId, long userId) throws GroupDoesNotExist {
        return groupRepository.findUserInGroup(groupId, userId);
    }
}

