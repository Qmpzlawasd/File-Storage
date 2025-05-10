package ro.unibuc.filespace.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ro.unibuc.filespace.Controller.FileMetadataController;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.FileMetadataNotPresent;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.FileMetadata;
import ro.unibuc.filespace.Service.FileMetadataService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileMetadataControllerUnitTest {

    @Mock
    private FileMetadataService fileMetadataService;

    @InjectMocks
    private FileMetadataController fileMetadataController;

    private FileMetadata testMetadata;

    @BeforeEach
    void setUp() {
        testMetadata = new FileMetadata();
        testMetadata.setExtension("txt");
        testMetadata.setSizeBytes(1024);
        testMetadata.setCrc32Checksum(123456789L);
    }

    @Test
    void getFileMetadata_Success() throws FileDoesNotExist, FileMetadataNotPresent, UserNotInGroup {
        // Arrange
        long groupId = 1L;
        long fileId = 1L;
        when(fileMetadataService.getFileMetadata(groupId, fileId)).thenReturn(testMetadata);

        // Act
        ResponseEntity<FileMetadata> response = fileMetadataController.getFileMetadata(groupId, fileId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testMetadata, response.getBody());
        verify(fileMetadataService, times(1)).getFileMetadata(groupId, fileId);
    }

    @Test
    void getFileMetadata_FileDoesNotExist() throws FileDoesNotExist, FileMetadataNotPresent, UserNotInGroup {
        // Arrange
        long groupId = 1L;
        long fileId = 1L;
        when(fileMetadataService.getFileMetadata(groupId, fileId)).thenThrow(FileDoesNotExist.class);

        // Act & Assert
        assertThrows(FileDoesNotExist.class, () -> {
            fileMetadataController.getFileMetadata(groupId, fileId);
        });
    }

    @Test
    void getFileMetadata_FileMetadataNotPresent() throws FileDoesNotExist, FileMetadataNotPresent, UserNotInGroup {
        // Arrange
        long groupId = 1L;
        long fileId = 1L;
        when(fileMetadataService.getFileMetadata(groupId, fileId)).thenThrow(FileMetadataNotPresent.class);

        // Act & Assert
        assertThrows(FileMetadataNotPresent.class, () -> {
            fileMetadataController.getFileMetadata(groupId, fileId);
        });
    }

    @Test
    void getFileMetadata_UserNotInGroup() throws FileDoesNotExist, FileMetadataNotPresent, UserNotInGroup {
        // Arrange
        long groupId = 1L;
        long fileId = 1L;
        when(fileMetadataService.getFileMetadata(groupId, fileId)).thenThrow(UserNotInGroup.class);

        // Act & Assert
        assertThrows(UserNotInGroup.class, () -> {
            fileMetadataController.getFileMetadata(groupId, fileId);
        });
    }
}