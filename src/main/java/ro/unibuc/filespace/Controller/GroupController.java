package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.unibuc.filespace.Service.GroupService;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class GroupController {
    private final GroupService groupService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("create_group")
    public ResponseEntity<Void> createGroup(@RequestParam String groupName) {
        groupService.createGroup(groupName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
