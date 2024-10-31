package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.filespace.Dto.UserDataDto;
import ro.unibuc.filespace.Exception.UserAlreadyExists;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Service.MembershipService;
import ro.unibuc.filespace.Service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final MembershipService membershipService;

    @GetMapping("/list_groups")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Group>> listGroups() throws Exception {
        List<Group> groups = membershipService.getJoinedGroups(userService.getAuthenticatedUser());
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/users")
    public ResponseEntity<Void> createAccount(@RequestBody UserDataDto createUserDto) {
        log.info("Attempting to create account with username {} and password {}", createUserDto.getUsername(), createUserDto.getPassword());
        try {
            userService.createUser(createUserDto.getUsername(), createUserDto.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (UserAlreadyExists e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
