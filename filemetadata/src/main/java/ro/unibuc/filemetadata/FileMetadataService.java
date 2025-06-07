package ro.unibuc.filemetadata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.filemetadata.Dto.FileRequestDto;
import ro.unibuc.filemetadata.Exception.FileMetadataNotPresent;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileMetadataService {
    private final FileMetadataRepository fileMetadataRepository;

    public FileMetadata getFileMetadata(long fileId) throws FileMetadataNotPresent {
        return fileMetadataRepository.findById(fileId).orElseThrow(FileMetadataNotPresent::new);
    }

    public void storeFileMetadata(FileRequestDto fileRequestDto) {
        String fileName = fileRequestDto.getFileName();
        String fileContent = fileRequestDto.getFileContent();

        String extension = extractExtension(fileName);
        int size = fileContent.length();
        long checksum = computeCRC32Checksum(fileContent.getBytes());

        log.info("Storing metadata for file: {}", fileName);
        fileMetadataRepository.save(new FileMetadata(fileRequestDto.getFileId(),fileRequestDto.getUserId(), extension, size, checksum));
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
