package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.sqlite.FileException;
import ro.unibuc.filespace.Exception.*;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Repository.FileMetadataRepository;
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

    public File storeFile(long groupId, MultipartFile file) throws UserNotInGroup, IOException, FileWithNameAlreadyExists, FileIsEmpty, GroupDoesNotExist {
        log.info("Attempting to store file '{}' in group {}", file.getOriginalFilename(), groupId);

        // check file not empty
        if (file.isEmpty()) {
            log.warn("File upload attempted with empty file");
            throw new FileIsEmpty();
        }

        // check group exists
        Group thisGroup = groupService.getGroup(groupId).orElseThrow(() -> {
            log.error("Group with ID {} not found", groupId);
            return new GroupDoesNotExist();
        });

        // check user is in group
        User thisUser = userService.getAuthenticatedUser();
        log.debug("Authenticated user: {}", thisUser.getUsername());

        groupService.getUserFromGroup(groupId, thisUser.getUserId()).orElseThrow(() -> {
            log.warn("User {} not found in group {}", thisUser.getUsername(), groupId);
            return new UserNotInGroup();
        });

        // check file name already there
        if (this.getFileFromGroup(groupId, file.getOriginalFilename()).isPresent()) {
            log.warn("File with name '{}' already exists in group {}", file.getOriginalFilename(), groupId);
            throw new FileWithNameAlreadyExists();
        }

        // upload file
        File newFile = new File(file.getOriginalFilename(), thisUser, new String(file.getBytes(), StandardCharsets.UTF_8));
        File storedFile = fileRepository.save(newFile);
        log.info("File '{}' saved with ID {}", storedFile.getFileName(), storedFile.getFileId());

        // add link between file and group
        storageService.addFileToGroup(storedFile, thisGroup);
        log.debug("File linked to group successfully");

        return storedFile;
    }

    public void deleteFileFromGroup(long groupId, String fileName) throws FileDoesNotExist, GroupDoesNotExist, UserNotInGroup {
        log.info("Attempting to delete file '{}' from group {}", fileName, groupId);

        groupService.getGroup(groupId).orElseThrow(() -> {
            log.error("Group with ID {} not found", groupId);
            return new GroupDoesNotExist();
        });

        User currentUser = userService.getAuthenticatedUser();
        groupService.getUserFromGroup(groupId, currentUser.getUserId()).orElseThrow(() -> {
            log.warn("User {} not found in group {}", currentUser.getUsername(), groupId);
            return new UserNotInGroup();
        });

        File thisFile = this.getFileFromGroup(groupId, fileName).orElseThrow(() -> {
            log.warn("File '{}' not found in group {}", fileName, groupId);
            return new FileDoesNotExist();
        });

        fileRepository.setFileToDeleted(thisFile.getFileId());
        log.info("File '{}' (ID: {}) marked as deleted", fileName, thisFile.getFileId());
    }

    public Optional<File> getFileFromGroup(long groupId, String fileName) throws UserNotInGroup {
        log.debug("Looking for file '{}' in group {}", fileName, groupId);

        User currentUser = userService.getAuthenticatedUser();
        groupService.getUserFromGroup(groupId, currentUser.getUserId()).orElseThrow(() -> {
            log.warn("User {} not authorized to access group {}", currentUser.getUsername(), groupId);
            return new UserNotInGroup();
        });

        Optional<File> file = fileRepository.findByFileNameAndGroupId(groupId, fileName);
        if (file.isEmpty()) {
            log.debug("File '{}' not found in group {}", fileName, groupId);
        } else {
            log.debug("File '{}' found in group {}", fileName, groupId);
        }
        return file;
    }

    public Optional<File> getFileFromGroupById(long groupId, long fileId) throws UserNotInGroup {
        log.debug("Looking for file ID {} in group {}", fileId, groupId);

        User currentUser = userService.getAuthenticatedUser();
        groupService.getUserFromGroup(groupId, currentUser.getUserId()).orElseThrow(() -> {
            log.warn("User {} not authorized to access group {}", currentUser.getUsername(), groupId);
            return new UserNotInGroup();
        });

        Optional<File> file = fileRepository.findByFileIdAndGroupId(groupId, fileId);
        if (file.isEmpty()) {
            log.debug("File ID {} not found in group {}", fileId, groupId);
        } else {
            log.debug("File ID {} found in group {}", fileId, groupId);
        }
        return file;
    }
}
