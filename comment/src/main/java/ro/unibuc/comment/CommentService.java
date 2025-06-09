package ro.unibuc.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ro.unibuc.comment.Dto.FileDTO;
import ro.unibuc.comment.Dto.UserDTO;
import ro.unibuc.comment.Exception.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final RestTemplate restTemplate;

    public Comment addComment(Long groupId, Long fileId, Long parentId, String commentContent) throws FileDoesNotExist, UserNotInGroup, CommentIsEmpty {
        if (commentContent == null || commentContent.trim().isEmpty()) {
            throw new CommentIsEmpty();
        }


        FileDTO file = restTemplate.getForEntity(
                "http://FILESPACE/api/{groupId}/files/{fileId}",
                FileDTO.class,
                groupId,
                fileId
        ).getBody();
        if (file == null) {
            throw new FileDoesNotExist();
        }

        if (parentId != null) {
            Optional<Comment> parentComment = commentRepository.findById(parentId);
            if (parentComment.isEmpty() || !parentComment.get().getFileId().equals(fileId)) {
                throw new IllegalArgumentException("Parent comment doesn't exist or doesn't belong to this file");
            }
        }


        UserDTO thisUser = restTemplate.getForEntity(
                "http://FILESPACE//api/users/authenticated",
                UserDTO.class
        ).getBody();
        if (thisUser == null) {
            throw new UserDoesNotExist();
        }


        Comment comment = new Comment(
                commentContent,
                file.getFileId(),
                file.getUserId(),
                thisUser.getUserId(),
                parentId,
                LocalDateTime.now()
        );

        log.info("Adding {} comment to file {} in group {}",
                parentId == null ? "root" : "reply",
                fileId,
                groupId);

        return commentRepository.save(comment);
    }

    public Comment editComment(long groupId, long fileId, Long commentId, String commentContent) throws CommentIsEmpty, UserNotInGroup, FileDoesNotExist, CommentDoesNotExist {
        if (commentContent == null || commentContent.trim().isEmpty()) {
            throw new CommentIsEmpty();
        }

        FileDTO file = restTemplate.getForEntity(
                "http://FILESPACE/api/{groupId}/files/{fileId}",
                FileDTO.class,
                groupId,
                fileId
        ).getBody();
        if (file == null) {
            throw new FileDoesNotExist();
        }

        UserDTO thisUser = restTemplate.getForEntity(
                "http://FILESPACE//api/users/authenticated",
                UserDTO.class
        ).getBody();
        if (thisUser == null) {
            throw new UserDoesNotExist();
        }

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentDoesNotExist::new);
        if (!comment.getCommenterId().equals(thisUser.getUserId())) {
            throw new CommentDoesNotExist();
        }

        int worked = commentRepository.updateCommentContent(commentId, thisUser.getUserId(), commentContent);
        if (worked == 0) {
            throw new CommentDoesNotExist();
        }

        return commentRepository.findById(commentId).orElseThrow(CommentDoesNotExist::new);
    }

    public Page<Comment> getComments(long groupId, long fileId, Pageable pageable) throws FileDoesNotExist {
        FileDTO file = restTemplate.getForEntity(
                "http://FILESPACE/api/{groupId}/files/{fileId}",
                FileDTO.class,
                groupId,
                fileId
        ).getBody();
        if (file == null) {
            throw new FileDoesNotExist();
        }
        return commentRepository.getCommentThread(fileId, pageable);
    }
}