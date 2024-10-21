package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.sqlite.FileException;
import ro.unibuc.filespace.Exception.FileWithNameAlreadyExists;
import ro.unibuc.filespace.Exception.GroupDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.File;
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


    public void storeFile(long groupId, MultipartFile file) throws GroupDoesNotExist, UserNotInGroup, IOException, FileWithNameAlreadyExists, FileException, GroupDoesNotExist{
        // check file not empty
        if (file.isEmpty()) {
            throw new FileException("File is empty");
        }

        // check group exists
        groupService.getGroup(groupId);

        // check user is in group
        User thisUser = userService.getAuthenticatedUser();
        groupService.getUserFromGroup(groupId, thisUser.getUserId()).orElseThrow(UserNotInGroup::new);

        // check file name already there
        if (this.getFileFromGroup(groupId, file.getOriginalFilename()).isPresent()){
            throw new FileWithNameAlreadyExists();
        }

        // upload file
        File newFile = new File(file.getOriginalFilename(), thisUser, new String(file.getBytes(), StandardCharsets.UTF_8));
        fileRepository.save(newFile);

        // TODO: LINK FILE TO GROUP

    }

    Optional<File> getFileFromGroup(long groupId, String fileName) {
        return fileRepository.findByFileNameAndGroupId(groupId, fileName);
    }
}
