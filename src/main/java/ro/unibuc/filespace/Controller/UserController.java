package ro.unibuc.filespace.Controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.filespace.Dto.UserDataDto;
import ro.unibuc.filespace.Exception.UserAlreadyExists;
import ro.unibuc.filespace.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequiredArgsConstructor
@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Tag(name = "post", description = "Make a user")
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
