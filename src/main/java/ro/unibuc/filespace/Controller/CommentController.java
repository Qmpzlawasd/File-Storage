package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.Comment;
import ro.unibuc.filespace.Service.CommentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @RequestMapping(value = {"/{groupId}/{fileId}/comment/{parentId}", "/{groupId}/{fileId}/comment"}, method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comment> addComment(@PathVariable Long groupId, @PathVariable Long fileId, @PathVariable(required = false)  Long parentId, @RequestParam("comment") String commentContent) throws FileDoesNotExist, UserNotInGroup {
        Comment comment = commentService.addComment(groupId, fileId, parentId, commentContent);
        return ResponseEntity.ok(comment);
    }

    @RequestMapping(value = "/{groupId}/{fileId}/comments", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Comment>> getFileComments(@PathVariable Long groupId, @PathVariable Long fileId) throws FileDoesNotExist, UserNotInGroup {
        return ResponseEntity.ok(commentService.getComments(groupId, fileId));
    }
}
