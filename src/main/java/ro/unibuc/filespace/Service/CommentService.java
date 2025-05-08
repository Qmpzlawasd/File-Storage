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
    private final UserService userService;
    private final FileService fileService;

    public Comment addComment(Long groupId, Long fileId, Long parentId, String commentContent) throws FileDoesNotExist, UserNotInGroup {
        if (commentContent == null || commentContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }

        File file = fileService.getFileFromGroupById(groupId, fileId)
                .orElseThrow(FileDoesNotExist::new);

        if (parentId != null) {
            Optional<Comment> parentComment = commentRepository.findById(parentId);
            if (parentComment.isEmpty() || !parentComment.get().getFile().getFileId().equals(fileId)) {
                throw new IllegalArgumentException("Parent comment doesn't exist or doesn't belong to this file");
            }
        }

        Comment comment = new Comment(
                commentContent,
                file,
                userService.getAuthenticatedUser(),
                parentId,
                LocalDateTime.now()
        );

        log.info("Adding {} comment to file {} in group {}",
                parentId == null ? "root" : "reply",
                fileId,
                groupId);

        return commentRepository.save(comment);
    }

    public List<Comment> getComments(long groupId, long fileId) throws FileDoesNotExist, UserNotInGroup {
        File file = fileService.getFileFromGroupById(groupId, fileId).orElseThrow(FileDoesNotExist::new);
        return commentRepository.getCommentThread(groupId, fileId);
    }
}
