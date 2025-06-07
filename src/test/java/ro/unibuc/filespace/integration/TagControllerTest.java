package ro.unibuc.filespace.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.Tag;
import ro.unibuc.filespace.Service.FileService;
import ro.unibuc.filespace.Service.FileMetadataService;
import ro.unibuc.filespace.Service.TagService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TagControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private FileService fileService;

    @Mock
    private FileMetadataService fileMetadataService;

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;

    private final Long groupId = 1L;
    private final Long fileId = 1L;
    private final String tagName = "test-tag";
    private final Tag testTag = new Tag(tagName);

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();
    }

    @Test
    public void testAddTag_Success() throws Exception {
        // Arrange
        when(tagService.addTag(eq(groupId), eq(fileId), anyString())).thenReturn(testTag);

        // Act & Assert
        mockMvc.perform(post("/api/{groupId}/{fileId}/tag", groupId, fileId)
                        .param("tagName", tagName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagName").value(tagName));
    }

    @Test
    public void testAddTag_FileDoesNotExist() throws Exception {
        // Arrange
        when(tagService.addTag(eq(groupId), eq(fileId), anyString())).thenThrow(new FileDoesNotExist());

        // Act & Assert
        mockMvc.perform(post("/api/{groupId}/{fileId}/tag", groupId, fileId)
                        .param("tagName", tagName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddTag_UserNotInGroup() throws Exception {
        // Arrange
        when(tagService.addTag(eq(groupId), eq(fileId), anyString())).thenThrow(new UserNotInGroup());

        // Act & Assert
        mockMvc.perform(post("/api/{groupId}/{fileId}/tag", groupId, fileId)
                        .param("tagName", tagName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetFileTags_Success() throws Exception {
        // Arrange
        List<Tag> tags = Collections.singletonList(testTag);
        when(tagService.getFileTags(groupId, fileId)).thenReturn(tags);

        // Act & Assert
        mockMvc.perform(get("/api/{groupId}/{fileId}/tags", groupId, fileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tagName").value(tagName));
    }

    @Test
    public void testGetFileTags_FileDoesNotExist() throws Exception {
        // Arrange
        when(tagService.getFileTags(groupId, fileId)).thenThrow(new FileDoesNotExist());

        // Act & Assert
        mockMvc.perform(get("/api/{groupId}/{fileId}/tags", groupId, fileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetGroupTags_Success() throws Exception {
        // Arrange
        List<Tag> tags = Collections.singletonList(testTag);
        when(tagService.getGroupTags(groupId)).thenReturn(tags);

        // Act & Assert
        mockMvc.perform(get("/api/{groupId}/tags", groupId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tagName").value(tagName));
    }

    @Test
    public void testGetGroupTags_UserNotInGroup() throws Exception {
        // Arrange
        when(tagService.getGroupTags(groupId)).thenThrow(new UserNotInGroup());

        // Act & Assert
        mockMvc.perform(get("/api/{groupId}/tags", groupId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}