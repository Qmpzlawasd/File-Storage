package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.FileMetadataNotPresent;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.FileMetadata;
import ro.unibuc.filespace.Service.FileMetadataService;

@RequiredArgsConstructor
@RestController
public class FileMetadataController {
    private static final Logger log = LoggerFactory.getLogger(FileMetadataController.class);
    private final FileMetadataService fileMetadataService;

    @RequestMapping(value = "/{groupId}/files/{fileId}", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileMetadata> getFileMetadata(@PathVariable long groupId, @PathVariable long fileId) throws FileDoesNotExist, FileMetadataNotPresent, UserNotInGroup {
        FileMetadata metadata = fileMetadataService.getFileMetadata(groupId, fileId);
        return ResponseEntity.ok(metadata);
    }
}
