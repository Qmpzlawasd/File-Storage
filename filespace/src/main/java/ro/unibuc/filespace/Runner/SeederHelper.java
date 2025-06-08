package ro.unibuc.filespace.Runner;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.sqlite.FileException;
import ro.unibuc.filespace.Dto.FileMetadataResult;
import ro.unibuc.filespace.Dto.FileRequestDto;
import ro.unibuc.filespace.Exception.*;
import ro.unibuc.filespace.Model.Comment;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Repository.CommentRepository;
import ro.unibuc.filespace.Service.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SeederHelper {
    private final UserService userService;
    private final GroupService groupService;
    private final FileService fileService;

//    private final FileMetadataService fileMetadataService;
    private final RestTemplate  restTemplate;
    private final CommentService commentService;

    User createUser(String username, String password) throws UserAlreadyExists {
        return userService.createUser(username, password);
    }

    Group createGroup(String name, User user) throws GroupAlreadyExists {
        return groupService.createGroup(name, user);
    }

    MultipartFile createMockMultipartFile(String fileName, byte[] content) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return fileName;
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                return "application/octet-stream";
            }

            @Override
            public boolean isEmpty() {
                return content.length == 0;
            }

            @Override
            public long getSize() {
                return content.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return content;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public Resource getResource() {
                return MultipartFile.super.getResource();
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
            }

            @Override
            public void transferTo(Path dest) throws IOException, IllegalStateException {
                MultipartFile.super.transferTo(dest);
            }
        };

    }

    File createFile(Group group, MultipartFile file) throws GroupAlreadyExists, IOException, UserNotInGroup, FileWithNameAlreadyExists, FileIsEmpty {
        return fileService.storeFile(group.getGroupId(), file);
    }

    void storeFileMetadata(File file) {
        FileRequestDto fileRequestDto = new FileRequestDto(file.getUserId(),file.getFileId(), file.getFileName(), file.getFileContent());
        restTemplate.postForObject("http://filemetadata/api/metadata", fileRequestDto, Void.class);
    }

    void fill100FilesComments(Group group) throws FileIsEmpty, IOException, UserNotInGroup, FileWithNameAlreadyExists, FileDoesNotExist, CommentIsEmpty, CommentDoesNotExist, InterruptedException {
        int times = 1;
        for (int i = 0; i < times; i++) {
            File f = fileService.storeFile(group.getGroupId(), createMockMultipartFile("asd" + i, "AsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasdAsdadsasd".getBytes()));
            Comment comment = null;
            for (int j = 0; j < times; j++) {
                comment = commentService.addComment(group.getGroupId(), f.getFileId(), null, "testsetsetse " + j);
            }
            commentService.editComment(group.getGroupId(), f.getFileId(), comment.getCommentId(), "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            commentService.getComments(group.getGroupId(), f.getFileId(), null);
        }
    }

    void add10Groups() throws FileDoesNotExist, FileIsEmpty, CommentIsEmpty, IOException, UserNotInGroup, FileWithNameAlreadyExists, CommentDoesNotExist, InterruptedException {
        for (int i = 0; i < 10; i++) {
            Group group1 = groupService.createGroup("Test " + i);
            fill100FilesComments(group1);
        }
    }
}
