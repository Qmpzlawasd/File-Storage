package ro.unibuc.filespace.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ro.unibuc.filespace.Exception.CommentIsEmpty;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.Comment;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Repository.CommentRepository;
import ro.unibuc.filespace.Service.CommentService;
import ro.unibuc.filespace.Service.FileService;
import ro.unibuc.filespace.Service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @Mock
    private FileService fileService;

    @InjectMocks
    private CommentService commentService;

    private File testFile;
    private User testUser;
    private Comment testComment;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);

        testFile = new File();
        testFile.setFileId(1L);
        testFile.setUserId(testUser.getUserId());

        testComment = new Comment(
                "Test comment",
                testFile.getFileId(),
                testUser.getUserId(),
                null,
                LocalDateTime.now()
        );
        testComment.setCommentId(1L);
    }

    @Test
    void addComment_shouldThrowFileDoesNotExist() throws UserNotInGroup {
        when(fileService.getFileFromGroupById(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(FileDoesNotExist.class,
                () -> commentService.addComment(1L, 1L, null, "Valid comment"));
    }

    @Test
    void addComment_shouldThrowExceptionForInvalidParentComment() throws UserNotInGroup {
        when(fileService.getFileFromGroupById(1L, 1L)).thenReturn(Optional.of(testFile));
        when(commentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> commentService.addComment(1L, 1L, 2L, "Valid comment"));
    }

    @Test
    void addComment_shouldCreateRootComment() throws FileDoesNotExist, UserNotInGroup, CommentIsEmpty {
        when(fileService.getFileFromGroupById(1L, 1L)).thenReturn(Optional.of(testFile));
        when(userService.getAuthenticatedUser()).thenReturn(testUser);
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        Comment result = commentService.addComment(1L, 1L, null, "Valid comment");

        assertNotNull(result);
        assertEquals("Test comment", result.getComment());
        assertNull(result.getParentCommentId());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void getComments_shouldThrowFileDoesNotExist() throws UserNotInGroup {
        when(fileService.getFileFromGroupById(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(FileDoesNotExist.class,
                () -> commentService.getComments(1L, 1L, Pageable.unpaged()));
    }

    @Test
    void getComments_shouldReturnPageOfComments() throws FileDoesNotExist, UserNotInGroup {
        Page<Comment> expectedPage = new PageImpl<>(Collections.singletonList(testComment));

        when(fileService.getFileFromGroupById(1L, 1L)).thenReturn(Optional.of(testFile));
        when(commentRepository.getCommentThread(1L, 1L, Pageable.unpaged())).thenReturn(expectedPage);

        Page<Comment> result = commentService.getComments(1L, 1L, Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        assertEquals(testComment, result.getContent().get(0));
    }
}