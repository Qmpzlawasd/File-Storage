package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.filespace.Exception.GroupDoesNotExist;
import ro.unibuc.filespace.Exception.UserDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Service.GroupService;
import ro.unibuc.filespace.Service.MembershipService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GroupController {
    private final GroupService groupService;
    private final MembershipService membershipService;

    @PostMapping("create_group")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> createGroup(@RequestParam String groupName) {
        groupService.createGroup(groupName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/{groupId}/list_members", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<User>> getUsersInGroup(@PathVariable long groupId) throws UserNotInGroup {
        List<User> users = groupService.getGroupUsers(groupId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/invite")
    public ResponseEntity<String> invite(@RequestParam String groupName, @RequestParam String username) throws Exception {
        String invite = groupService.generateInvitation(groupName, username);
        return ResponseEntity.ok(invite);
    }

    @GetMapping("/accept_invite")
    public ResponseEntity<Void> acceptInvite(@RequestParam String inviteToken) {
        try {
            groupService.acceptInvitation(inviteToken);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
