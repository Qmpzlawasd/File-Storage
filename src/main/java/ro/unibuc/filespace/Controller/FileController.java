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
import ro.unibuc.filespace.Service.FileService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@RequiredArgsConstructor
@RestController
public class FileController {
    private final FileService fileService;

    @RequestMapping(value = "/{groupId}/upload", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> uploadFile(@PathVariable long groupId, @RequestParam("file") MultipartFile file) throws IOException {
//        try {
            uploadFile(groupId, file);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
