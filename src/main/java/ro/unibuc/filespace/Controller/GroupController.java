package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.filespace.Exception.GroupDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.GroupTag;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Service.GroupService;
import ro.unibuc.filespace.Service.MembershipService;
import ro.unibuc.filespace.Service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GroupController {
    private final GroupService groupService;
    private final MembershipService membershipService;
    private final UserService userService;

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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> invite(@RequestParam String groupName, @RequestParam String username) throws Exception {
        Group group = groupService.getGroup(groupName).orElseThrow(GroupDoesNotExist::new);
        groupService.getUserFromGroup(group.getGroupId(), userService.getAuthenticatedUser().getUserId());
        String invite = groupService.generateInvitation(groupName, username);
        return ResponseEntity.ok(invite);
    }

    @GetMapping("/accept_invite")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> acceptInvite(@RequestParam String inviteToken) throws Exception {
            groupService.acceptInvitation(inviteToken);
            return ResponseEntity.ok().build();
    }
}
