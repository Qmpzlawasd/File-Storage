package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Service.FileMetadataService;
import ro.unibuc.filespace.Service.FileService;
import ro.unibuc.filespace.Service.StorageService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FileController {
    private final FileService fileService;
    private final StorageService storageService;
    private final FileMetadataService fileMetadataService;

    @RequestMapping(value = "/{groupId}/upload", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> uploadFile(@PathVariable long groupId, @RequestParam("file") MultipartFile file) {
        try {
            File storedFile = fileService.storeFile(groupId, file, null);
            fileMetadataService.storeFileMetadata(storedFile);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/{groupId}/files", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<File>> getGroupFiles(@PathVariable long groupId) throws UserNotInGroup {
        List<File> files;
        files = storageService.getFilesFromGroup(groupId);
        return ResponseEntity.ok(files);
    }

    @RequestMapping(value = "/{groupId}/delete_file", method = RequestMethod.DELETE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteFile(@PathVariable long groupId, @RequestParam String fileName) throws FileDoesNotExist, UserNotInGroup {
        fileService.deleteFileFromGroup(groupId, fileName);
        return ResponseEntity.ok().build();
    }
}
