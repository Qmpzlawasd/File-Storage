package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.filespace.Dto.UserDTO;
import ro.unibuc.filespace.Dto.UserDataDto;
import ro.unibuc.filespace.Exception.UserDoesNotExist;
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

    @GetMapping("/api/list_groups")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Group>> listGroups() {
        List<Group> groups = membershipService.getJoinedGroups(userService.getAuthenticatedUser());
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/api/sign-up")
    public ResponseEntity<Void> createAccount(@RequestBody UserDataDto createUserDto) {
        log.info("Attempting to create account with username {} and password {}", createUserDto.getUsername(), createUserDto.getPassword());
            userService.createUser(createUserDto.getUsername(), createUserDto.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/api/users/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId)  {
        log.debug("Fetching user with ID: {}", userId);
        UserDTO user = userService.getUserDtoById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/api/users/authenticated")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getAuthenticatedUser()   {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.debug("Fetching authenticated user: {}", username);

        UserDTO user = userService.getAuthenticatedUserDto();
        if (user == null) {
            throw new UserDoesNotExist();
        }
        return ResponseEntity.ok(user);
    }
}
