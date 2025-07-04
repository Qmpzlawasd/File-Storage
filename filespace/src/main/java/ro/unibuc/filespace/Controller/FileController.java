package ro.unibuc.filespace.Controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ro.unibuc.filespace.Dto.FileDTO;
import ro.unibuc.filespace.Dto.FileMetadataResult;
import ro.unibuc.filespace.Dto.FileRequestDto;
import ro.unibuc.filespace.Dto.FileResponseDTO;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.FileIsEmpty;
import ro.unibuc.filespace.Exception.FileWithNameAlreadyExists;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Service.FileService;
import ro.unibuc.filespace.Service.StorageService;
import ro.unibuc.filespace.Service.UserService;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class FileController {
    private final FileService fileService;

    private final StorageService storageService;

    private final RestTemplate restTemplate;

    private final UserService userService;


    @RequestMapping(value = "/api/{groupId}/upload", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> uploadFile(@PathVariable long groupId, @RequestParam("file") MultipartFile file1) throws FileIsEmpty, UserNotInGroup, FileWithNameAlreadyExists, IOException {
        File file = fileService.storeFile(groupId, file1);
        FileRequestDto fileRequestDto = new FileRequestDto(file.getFileId(), userService.getAuthenticatedUser().getUserId(),file.getFileName(), file.getFileContent());
        ResponseEntity<FileMetadataResult> response = restTemplate.postForEntity(
                "http://FILEMETADATA/api/metadata",
                fileRequestDto,
                FileMetadataResult.class
        );

        log.info("Received from metadata service: {}", response.getBody());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/api/{groupId}/files", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FileResponseDTO>> getGroupFiles(@PathVariable long groupId) throws UserNotInGroup {
        List<File>  files = storageService.getFilesFromGroup(groupId);
        return ResponseEntity.ok(fileService.mapFilesToFileDTO(files));
    }

    @RequestMapping(value = "/api/{groupId}/get-files", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FileResponseDTO>> getGroupFilesById(@PathVariable long groupId) throws UserNotInGroup {
        List<File>  files = storageService.getFilesFromGroup(groupId);
        return ResponseEntity.ok(fileService.mapFilesToFileDTO(files));
    }

    @RequestMapping(value = "/api/{groupId}/delete_file", method = RequestMethod.DELETE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteFile(@PathVariable long groupId, @RequestParam String fileName) throws FileDoesNotExist, UserNotInGroup {
        fileService.deleteFileFromGroup(groupId, fileName);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/api/{groupId}/files/{fileId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileDTO> getFileById(@PathVariable long groupId, @PathVariable long fileId)
            throws UserNotInGroup, FileDoesNotExist {
        File file = fileService.getFileFromGroupById(groupId, fileId)
                .orElseThrow(FileDoesNotExist::new);
        FileDTO responseDTO = new FileDTO(
                file.getFileId(),
                file.getUserId(),
                file.getFileName(),
                file.getFileContent(),
                file.isDeleted());
        return ResponseEntity.ok(responseDTO);
    }
}
