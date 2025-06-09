package ro.unibuc.filespace.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.GroupDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.*;
import ro.unibuc.filespace.Repository.GroupTagRepository;
import ro.unibuc.filespace.Repository.TagRepository;
import ro.unibuc.filespace.Service.FileService;
import ro.unibuc.filespace.Service.GroupService;
import ro.unibuc.filespace.Service.TagService;
import ro.unibuc.filespace.Service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private GroupTagRepository groupTagRepository;

    @Mock
    private FileService fileService;

    @Mock
    private GroupService groupService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TagService tagService;

    private final Long groupId = 1L;
    private final Long fileId = 1L;
    private final String tagName = "test-tag";

    @Test
    public void testAddTag_Success() throws FileDoesNotExist, UserNotInGroup {
        // Arrange
        File mockFile = new File();
        Group mockGroup = new Group();
        when(fileService.getFileFromGroupById(groupId, fileId)).thenReturn(Optional.of(mockFile));
        when(groupService.getGroup(groupId)).thenReturn(Optional.of(mockGroup));
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(groupTagRepository.save(any(GroupTag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Tag result = tagService.addTag(groupId, fileId, tagName);

        // Assert
        assertNotNull(result);
        assertEquals(tagName, result.getTagName());
        verify(tagRepository, times(1)).save(any(Tag.class));
        verify(groupTagRepository, times(1)).save(any(GroupTag.class));
    }

    @Test
    public void testAddTag_FileDoesNotExist() throws UserNotInGroup {
        // Arrange
        when(fileService.getFileFromGroupById(groupId, fileId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FileDoesNotExist.class, () -> tagService.addTag(groupId, fileId, tagName));
    }

    @Test
    public void testAddTag_GroupDoesNotExist() throws UserNotInGroup {
        // Arrange
        when(fileService.getFileFromGroupById(groupId, fileId)).thenReturn(Optional.of(new File()));
        when(groupService.getGroup(groupId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GroupDoesNotExist.class, () -> tagService.addTag(groupId, fileId, tagName));
    }

    @Test
    public void testGetFileTags_Success() throws UserNotInGroup, FileDoesNotExist {
        // Arrange
        when(fileService.getFileFromGroupById(groupId, fileId)).thenReturn(Optional.of(new File()));
        List<Tag> expectedTags = Collections.singletonList(new Tag(tagName));
        when(groupTagRepository.getFileTags(groupId, fileId)).thenReturn(expectedTags);

        // Act
        List<Tag> result = tagService.getFileTags(groupId, fileId);

        // Assert
        assertEquals(expectedTags, result);
    }

    @Test
    public void testGetFileTags_FileDoesNotExist() throws UserNotInGroup {
        // Arrange
        when(fileService.getFileFromGroupById(groupId, fileId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FileDoesNotExist.class, () -> tagService.getFileTags(groupId, fileId));
    }
}