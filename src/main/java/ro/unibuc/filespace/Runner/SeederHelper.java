package ro.unibuc.filespace.Runner;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.unibuc.filespace.Exception.UserAlreadyExists;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Service.UserService;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class SeederHelper {
    private final UserService userService;

    void createUser(String username, String password) throws UserAlreadyExists {
        userService.createUser(username, password);
    }
}
