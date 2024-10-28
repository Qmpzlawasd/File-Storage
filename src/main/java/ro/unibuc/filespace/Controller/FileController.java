package ro.unibuc.filespace.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sqlite.FileException;
import ro.unibuc.filespace.Exception.FileWithNameAlreadyExists;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Service.FileService;
import ro.unibuc.filespace.Service.StorageService;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FileController {
    private final FileService fileService;
    private final StorageService storageService;

    @RequestMapping(value = "/{groupId}/upload", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> uploadFile(@PathVariable long groupId, @RequestParam("file") MultipartFile file) {
        try {
            fileService.storeFile(groupId, file, null);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/{groupId}/files", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<File>> getGroupFiles(@PathVariable long groupId) {
        List<File> files;
        try {
            files = storageService.getFilesFromGroup(groupId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(files);
    }
}
