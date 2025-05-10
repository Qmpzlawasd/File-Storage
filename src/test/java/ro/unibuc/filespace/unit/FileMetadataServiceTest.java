package ro.unibuc.filespace.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.FileMetadataNotPresent;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.FileMetadata;
import ro.unibuc.filespace.Repository.FileMetadataRepository;
import ro.unibuc.filespace.Service.FileMetadataService;
import ro.unibuc.filespace.Service.FileService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileMetadataServiceTest {

    @Mock
    private FileMetadataRepository fileMetadataRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileMetadataService fileMetadataService;

    @Test
    void getFileMetadata_Success() throws FileDoesNotExist, FileMetadataNotPresent, UserNotInGroup {
        // Arrange
        long groupId = 1L;
        long fileId = 1L;
        FileMetadata expectedMetadata = new FileMetadata();

        when(fileService.getFileFromGroupById(groupId, fileId)).thenReturn(Optional.of(new File()));
        when(fileMetadataRepository.findFileMetadata(fileId)).thenReturn(Optional.of(expectedMetadata));

        // Act
        FileMetadata result = fileMetadataService.getFileMetadata(groupId, fileId);

        // Assert
        assertEquals(expectedMetadata, result);
        verify(fileService, times(1)).getFileFromGroupById(groupId, fileId);
        verify(fileMetadataRepository, times(1)).findFileMetadata(fileId);
    }

    @Test
    void getFileMetadata_FileDoesNotExist() throws UserNotInGroup {
        // Arrange
        long groupId = 1L;
        long fileId = 1L;

        when(fileService.getFileFromGroupById(groupId, fileId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FileDoesNotExist.class, () -> {
            fileMetadataService.getFileMetadata(groupId, fileId);
        });
    }

    @Test
    void getFileMetadata_FileMetadataNotPresent() throws FileDoesNotExist, UserNotInGroup {
        // Arrange
        long groupId = 1L;
        long fileId = 1L;

        when(fileService.getFileFromGroupById(groupId, fileId)).thenReturn(Optional.of(new File()));
        when(fileMetadataRepository.findFileMetadata(fileId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FileMetadataNotPresent.class, () -> {
            fileMetadataService.getFileMetadata(groupId, fileId);
        });
    }

    @Test
    void storeFileMetadata_Success() {
        // Arrange
        File file = new File();
        file.setFileName("test.txt");
        file.setFileContent("This is a test content");

        // Act
        fileMetadataService.storeFileMetadata(file);

        // Assert
        verify(fileMetadataRepository, times(1)).save(any(FileMetadata.class));
    }

    @Test
    void extractExtension_WithExtension() {
        // Arrange
        String fileName = "document.pdf";
        FileMetadataService service = new FileMetadataService(null, null);

        // Act
        String result = service.extractExtension(fileName);

        // Assert
        assertEquals("pdf", result);
    }

    @Test
    void extractExtension_WithoutExtension() {
        // Arrange
        String fileName = "document";
        FileMetadataService service = new FileMetadataService(null, null);

        // Act
        String result = service.extractExtension(fileName);

        // Assert
        assertNull(result);
    }

    @Test
    void computeCRC32Checksum_Success() {
        // Arrange
        byte[] data = "test data".getBytes();
        FileMetadataService service = new FileMetadataService(null, null);

        // Act
        long result = service.computeCRC32Checksum(data);

        // Assert
        assertTrue(result != 0); // Just verify it returns some checksum
    }
}