package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.filespace.Exception.CommentDoesNotExist;
import ro.unibuc.filespace.Exception.CommentIsEmpty;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.Comment;
import ro.unibuc.filespace.Repository.CommentRepository;
import ro.unibuc.filespace.Service.CommentService;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    @RequestMapping(value = {"/{groupId}/{fileId}/comment/{parentId}", "/{groupId}/{fileId}/comment"}, method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comment> addComment(@PathVariable Long groupId, @PathVariable Long fileId, @PathVariable(required = false) Long parentId, @RequestParam("comment") String commentContent) throws FileDoesNotExist, UserNotInGroup, CommentIsEmpty {
        Comment comment = commentService.addComment(groupId, fileId, parentId, commentContent);
        return ResponseEntity.ok(comment);
    }

    @RequestMapping(value = {"/{groupId}/{fileId}/comment/{commentId}"}, method = RequestMethod.PATCH)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comment> editComment(@PathVariable Long groupId, @PathVariable Long fileId, @PathVariable Long commentId, @RequestParam("comment") String commentContent) throws FileDoesNotExist, UserNotInGroup, CommentIsEmpty, CommentDoesNotExist {
        Comment comment = commentService.editComment(groupId, fileId, commentId, commentContent);
        return ResponseEntity.ok(comment);
    }

    @RequestMapping(value = "/{groupId}/{fileId}/comments", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<Comment>> getFileComments(
            @PathVariable Long groupId,
            @PathVariable Long fileId,
            @PageableDefault(size = 2, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) throws FileDoesNotExist, UserNotInGroup {
        return ResponseEntity.ok(commentService.getComments(groupId, fileId, pageable));
    }
}