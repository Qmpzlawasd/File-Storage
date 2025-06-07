package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.FileMetadataNotPresent;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.FileMetadata;
import ro.unibuc.filespace.Repository.FileMetadataRepository;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileMetadataService {
    private final FileMetadataRepository fileMetadataRepository;
    private final FileService fileService;

    public FileMetadata getFileMetadata(long groupId, long fileId) throws UserNotInGroup, FileDoesNotExist, FileMetadataNotPresent {
        fileService.getFileFromGroupById(groupId, fileId).orElseThrow(FileDoesNotExist::new);
        return fileMetadataRepository.findFileMetadata(fileId).orElseThrow(FileMetadataNotPresent::new);
    }

    public void storeFileMetadata(File file) {
        String fileName = file.getFileName();
        String fileContent = file.getFileContent();

        String extension = extractExtension(fileName);
        int size = fileContent.length();
        long checksum = computeCRC32Checksum(fileContent.getBytes());

        log.info("Storing metadata for file: {}", fileName);
        fileMetadataRepository.save(new FileMetadata(file.getFileId(),file.getUserId(), extension, size, checksum));
    }

    public String extractExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return (index > 0) ? fileName.substring(index + 1) : null;
    }

    public long computeCRC32Checksum(byte[] data) {
        Checksum crc32 = new CRC32();
        crc32.update(data, 0, data.length);
        return crc32.getValue();
    }
}
