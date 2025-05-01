package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.FileWithNameAlreadyExists;
import ro.unibuc.filespace.Exception.GroupDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.*;
import ro.unibuc.filespace.Repository.CommentRepository;
import ro.unibuc.filespace.Repository.FileRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final GroupService groupService;
    private final UserService userService;
    private final StorageService storageService;
    private final FileService fileService;

    public Comment addComment(long groupId, long fileId, long parentId, String commentContent) throws FileDoesNotExist, UserNotInGroup {
        File file = fileService.getFileFromGroupById(groupId, fileId).orElseThrow(FileDoesNotExist::new);
        return commentRepository.save(new Comment(commentContent, file, userService.getAuthenticatedUser(), parentId, LocalDateTime.now()));
    }

    public List<Comment> getComments(long groupId, long fileId) throws FileDoesNotExist {
        return commentRepository.getCommentThread(groupId, fileId);
    }
}
