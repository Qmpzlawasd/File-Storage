package ro.unibuc.filespace.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.unibuc.filespace.Dto.UserDataDto;
import ro.unibuc.filespace.Exception.UserAlreadyExists;
import ro.unibuc.filespace.Exception.UserDoesNotExist;
import ro.unibuc.filespace.Exception.UserWrongPassword;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String username, String password) throws UserAlreadyExists {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExists();
        }

        log.info("Creating a new account");
        final String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, hashedPassword);
        return userRepository.save(newUser);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findUserById(long userId) {
        return userRepository.findById(userId);
    }

    public String getAuthenticatedUsername() {
        log.info("Getting authenticated user account");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }

    public void checkUserCredentials(UserDataDto userDataDto) throws UserDoesNotExist, UserWrongPassword {
        User thisUser = this.findUserByUsername(userDataDto.getUsername()).orElseThrow(UserDoesNotExist::new);
        if (!passwordEncoder.matches(userDataDto.getPassword(), thisUser.getPassword())) {
            throw new UserWrongPassword();
        }
    }

    public User getAuthenticatedUser() throws UserDoesNotExist {
        return userRepository.findByUsername(this.getAuthenticatedUsername()).orElseThrow(UserDoesNotExist::new);
    }

    public List<Group> getGroups() throws UserDoesNotExist {
        return userRepository.findUserGroups(this.getAuthenticatedUser().getUserId());
    }
}
