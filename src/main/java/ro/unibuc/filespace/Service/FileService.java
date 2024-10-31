package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.sqlite.FileException;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.FileWithNameAlreadyExists;
import ro.unibuc.filespace.Exception.GroupDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Repository.FileRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final FileRepository fileRepository;
    private final GroupService groupService;
    private final UserService userService;
    private final StorageService storageService;

    public File storeFile(long groupId, MultipartFile file, User user) throws UserNotInGroup, IOException, FileWithNameAlreadyExists, FileException, GroupDoesNotExist {
        // check file not empty
        if (file.isEmpty()) {
            throw new FileException("File is empty");
        }

        // check group exists
        Group thisGroup = groupService.getGroup(groupId).orElseThrow(GroupDoesNotExist::new);

        // check user is in group
        User thisUser = user == null ? userService.getAuthenticatedUser() : user;
        groupService.getUserFromGroup(groupId, thisUser.getUserId()).orElseThrow(UserNotInGroup::new);

        // check file name already there
        if (this.getFileFromGroup(groupId, file.getOriginalFilename()).isPresent()) {
            throw new FileWithNameAlreadyExists();
        }

        // upload file
        File newFile = new File(file.getOriginalFilename(), thisUser, new String(file.getBytes(), StandardCharsets.UTF_8));
        File storedFile = fileRepository.save(newFile);

        // add link between file and group
        storageService.addFileToGroup(storedFile, thisGroup);
        return storedFile;
    }

    public void deleteFileFromGroup(long groupId, String fileName) throws FileDoesNotExist, GroupDoesNotExist, UserNotInGroup {
        groupService.getGroup(groupId).orElseThrow(GroupDoesNotExist::new);
        groupService.getUserFromGroup(groupId, userService.getAuthenticatedUser().getUserId()).orElseThrow(UserNotInGroup::new);
        File thisFile = this.getFileFromGroup(groupId, fileName).orElseThrow(FileDoesNotExist::new);
        fileRepository.setFileToDeleted(thisFile.getFileId());
    }

    public Optional<File> getFileFromGroup(long groupId, String fileName) {
        return fileRepository.findByFileNameAndGroupId(groupId, fileName);
    }
}
