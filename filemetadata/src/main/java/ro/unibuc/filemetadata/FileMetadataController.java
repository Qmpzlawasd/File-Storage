package ro.unibuc.filemetadata;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.filemetadata.Dto.FileMetadataResult;
import ro.unibuc.filemetadata.Dto.FileRequestDto;
import ro.unibuc.filemetadata.Exception.FileMetadataNotPresent;

@RequiredArgsConstructor
@RestController
public class FileMetadataController {

    private static final Logger log = LoggerFactory.getLogger(FileMetadataController.class);
    private final FileMetadataService fileMetadataService;

    @RequestMapping(value = "/api/files/{fileId}", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileMetadata> getFileMetadata(@PathVariable long fileId) throws FileMetadataNotPresent {
        log.info("Received request to fetch metadata for file ID: {}", fileId);
        FileMetadata metadata = fileMetadataService.getFileMetadata(fileId);
        log.info("Successfully fetched metadata for file ID: {}", fileId);
        return ResponseEntity.ok(metadata);
    }

    @RequestMapping(value = "/api/metadata", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileMetadataResult> storeFile(@RequestBody FileRequestDto fileRequestDto) {
        log.info("Received request to store file metadata: {}", fileRequestDto);
        FileMetadata metadata = fileMetadataService.storeFileMetadata(fileRequestDto);
        log.info("Successfully stored file metadata: {}", metadata);
        return ResponseEntity.ok(new FileMetadataResult(metadata.getExtension(), metadata.getSizeBytes(), metadata.getCrc32Checksum()));
    }
}
