package ro.unibuc.filespace.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.filespace.Controller.CommentController;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.Comment;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Service.CommentService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private Comment testComment;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();

        User testUser = new User();
        testUser.setUserId(1L);

        File testFile = new File();
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
    void addComment_shouldReturnCreatedComment() throws Exception {
        when(commentService.addComment(anyLong(), anyLong(), any(), anyString()))
                .thenReturn(testComment);

        mockMvc.perform(post("/api/1/1/comment")
                        .param("comment", "Test comment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Test comment"));
    }

    @Test
    void addComment_withParentId_shouldReturnCreatedComment() throws Exception {
        when(commentService.addComment(anyLong(), anyLong(), anyLong(), anyString()))
                .thenReturn(testComment);

        mockMvc.perform(post("/api/1/1/comment/2")
                        .param("comment", "Test reply")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Test comment"));
    }

    @Test
    void addComment_shouldHandleFileDoesNotExist() throws Exception {
        when(commentService.addComment(anyLong(), anyLong(), any(), anyString()))
                .thenThrow(new FileDoesNotExist());

        mockMvc.perform(post("/api/1/1/comment")
                        .param("comment", "Test comment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void addComment_shouldHandleUserNotInGroup() throws Exception {
        when(commentService.addComment(anyLong(), anyLong(), any(), anyString()))
                .thenThrow(new UserNotInGroup());

        mockMvc.perform(post("/api/1/1/comment")
                        .param("comment", "Test comment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}