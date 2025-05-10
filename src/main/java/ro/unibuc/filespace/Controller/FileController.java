package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sqlite.FileException;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.FileIsEmpty;
import ro.unibuc.filespace.Exception.FileWithNameAlreadyExists;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.FileMetadata;
import ro.unibuc.filespace.Service.FileMetadataService;
import ro.unibuc.filespace.Service.FileService;
import ro.unibuc.filespace.Service.StorageService;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FileController {
    private final FileService fileService;
    private final StorageService storageService;
    private final FileMetadataService fileMetadataService;

    @RequestMapping(value = "/{groupId}/upload", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> uploadFile(@PathVariable long groupId, @RequestParam("file") MultipartFile file) throws FileIsEmpty, IOException, UserNotInGroup, FileWithNameAlreadyExists {
        File file1 = fileService.storeFile(groupId, file);
        fileMetadataService.storeFileMetadata(file1);
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
