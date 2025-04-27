package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    public void storeFileMetadata(File file) {
        final String thisFileName = file.getFileName();
        final String thisFileContent = file.getFileContent();

        String thisExtension = null;
        int i = thisFileName.lastIndexOf('.');
        if (i > 0) {
            thisExtension = thisFileName.substring(i + 1);
        }

        fileMetadataRepository.save(new FileMetadata(file, thisExtension, thisFileContent.length(), getCRC32Checksum(thisFileContent.getBytes())));
    }

    private static long getCRC32Checksum(byte[] fileBytes) {
        Checksum crc32 = new CRC32();
        crc32.update(fileBytes, 0, fileBytes.length);
        return crc32.getValue();
    }
}
