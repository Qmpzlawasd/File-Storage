package ro.unibuc.filespace.unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import org.sqlite.FileException;
import ro.unibuc.filespace.Exception.*;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Repository.FileRepository;
import ro.unibuc.filespace.Service.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private GroupService groupService;

    @Mock
    private UserService userService;

    @Mock
    private StorageService storageService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStoreFile_Success() throws Exception {
        long groupId = 1L;
        String fileName = "testFile.txt";
        User user = new User(1L, "testUser", "password");
        Group group = new Group("testGroup");
        group.setGroupId(groupId);

        File expectedFile = new File(fileName, user, "file content");

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn(fileName);
        when(multipartFile.getBytes()).thenReturn("file content".getBytes(StandardCharsets.UTF_8));
        when(groupService.getGroup(groupId)).thenReturn(Optional.of(group));
        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(groupService.getUserFromGroup(groupId, user.getUserId())).thenReturn(Optional.of(user));
        when(fileRepository.findByFileNameAndGroupId(groupId, fileName)).thenReturn(Optional.empty());
        when(fileRepository.save(any(File.class))).thenReturn(expectedFile);

        File storedFile = fileService.storeFile(groupId, multipartFile, user);

        assertNotNull(storedFile);
        assertEquals(fileName, storedFile.getFile_name());
        assertEquals("file content", storedFile.getFile_content());
        verify(storageService, times(1)).addFileToGroup(storedFile, group);
    }

    @Test
    public void testStoreFile_FileIsEmpty() {
        long groupId = 1L;
        User user = new User(1L, "testUser", "password");

        when(multipartFile.isEmpty()).thenReturn(true);

        assertThrows(FileException.class, () -> fileService.storeFile(groupId, multipartFile, user));
    }

    @Test
    public void testStoreFile_FileWithNameAlreadyExists() {
        long groupId = 1L;
        String fileName = "testFile.txt";
        User user = new User(1L, "testUser", "password");
        Group group = new Group("testGroup");
        group.setGroupId(groupId);

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn(fileName);
        when(groupService.getGroup(groupId)).thenReturn(Optional.of(group));
        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(groupService.getUserFromGroup(groupId, user.getUserId())).thenReturn(Optional.of(user));
        when(fileRepository.findByFileNameAndGroupId(groupId, fileName)).thenReturn(Optional.of(new File()));

        assertThrows(FileWithNameAlreadyExists.class, () -> fileService.storeFile(groupId, multipartFile, user));
    }

    @Test
    public void testDeleteFileFromGroup_Success() throws Exception {
        long groupId = 1L;
        String fileName = "testFile.txt";
        User user = new User(1L, "testUser", "password");
        File file = new File(fileName, user, "file content");
        file.setFileId(1L);

        when(groupService.getGroup(groupId)).thenReturn(Optional.of(new Group("testGroup")));
        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(groupService.getUserFromGroup(groupId, user.getUserId())).thenReturn(Optional.of(user));
        when(fileRepository.findByFileNameAndGroupId(groupId, fileName)).thenReturn(Optional.of(file));

        fileService.deleteFileFromGroup(groupId, fileName);

        verify(fileRepository, times(1)).setFileToDeleted(file.getFileId());
    }

    @Test
    public void testDeleteFileFromGroup_FileDoesNotExist() {
        long groupId = 1L;
        String fileName = "nonExistentFile.txt";
        User user = new User(1L, "testUser", "password");

        when(groupService.getGroup(groupId)).thenReturn(Optional.of(new Group("testGroup")));
        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(groupService.getUserFromGroup(groupId, user.getUserId())).thenReturn(Optional.of(user));
        when(fileRepository.findByFileNameAndGroupId(groupId, fileName)).thenReturn(Optional.empty());

        assertThrows(FileDoesNotExist.class, () -> fileService.deleteFileFromGroup(groupId, fileName));
    }

    @Test
    public void testGetFileFromGroup_Success() {
        long groupId = 1L;
        String fileName = "testFile.txt";
        File file = new File(fileName, new User(), "file content");

        when(fileRepository.findByFileNameAndGroupId(groupId, fileName)).thenReturn(Optional.of(file));

        Optional<File> result = fileService.getFileFromGroup(groupId, fileName);

        assertTrue(result.isPresent());
        assertEquals(fileName, result.get().getFile_name());
    }
}
