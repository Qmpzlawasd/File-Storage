package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Model.Comment;
import ro.unibuc.filespace.Model.Tag;
import ro.unibuc.filespace.Service.FileMetadataService;
import ro.unibuc.filespace.Service.FileService;
import ro.unibuc.filespace.Service.StorageService;
import ro.unibuc.filespace.Service.TagService;

import java.awt.*;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class TagController {
    private final FileService fileService;
    private final FileMetadataService fileMetadataService;
    private final TagService tagService;

    @RequestMapping(value = "/{groupId}/{fileId}/tag", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Tag> addTag(@PathVariable long groupId, @PathVariable long fileId, String tagName) {
        return ResponseEntity.ok(tagService.addTag(groupId, fileId, tagName));
    }
}
