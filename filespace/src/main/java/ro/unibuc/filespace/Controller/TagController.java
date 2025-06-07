package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ro.unibuc.filespace.Exception.FileDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.Tag;
import ro.unibuc.filespace.Service.FileService;
import ro.unibuc.filespace.Service.TagService;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class TagController {
    private final FileService fileService;
    private final RestTemplate restTemplate;
    private final TagService tagService;

    @RequestMapping(value = "/api/{groupId}/{fileId}/tag", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Tag> addTag(@PathVariable long groupId, @PathVariable long fileId, String tagName) throws FileDoesNotExist, UserNotInGroup {
        return ResponseEntity.ok(tagService.addTag(groupId, fileId, tagName));
    }

    @RequestMapping(value = "/api/{groupId}/{fileId}/tags", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Tag>> getFileTags(@PathVariable long groupId, @PathVariable long fileId) throws FileDoesNotExist, UserNotInGroup {
        return ResponseEntity.ok(tagService.getFileTags(groupId, fileId));
    }

    @RequestMapping(value = "/api/{groupId}/tags", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Tag>> getGroupTags(@PathVariable long groupId) throws FileDoesNotExist, UserNotInGroup {
        return ResponseEntity.ok(tagService.getGroupTags(groupId));
    }
}
