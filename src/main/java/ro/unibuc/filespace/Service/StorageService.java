package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.filespace.Exception.GroupDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.*;
import ro.unibuc.filespace.Repository.StorageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {
    private final StorageRepository storageRepository;
    private final GroupService groupService;
    private final UserService userService;

    public Storage addFileToGroup(File file, Group group) {
        return storageRepository.save(new Storage(group.getGroupId(), file.getFileId() , file.getUserId()));
    }

    public List<File> getFilesFromGroup(long groupId) throws UserNotInGroup {
        groupService.getUserFromGroup(groupId, userService.getAuthenticatedUser().getUserId()).orElseThrow(UserNotInGroup::new);
        return storageRepository.findFilesByGroup(groupId);
    }
}
