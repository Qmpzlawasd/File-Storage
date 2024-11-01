package ro.unibuc.filespace.unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.unibuc.filespace.Dto.UserDataDto;
import ro.unibuc.filespace.Exception.UserAlreadyExists;
import ro.unibuc.filespace.Exception.UserDoesNotExist;
import ro.unibuc.filespace.Exception.UserWrongPassword;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Repository.UserRepository;
import ro.unibuc.filespace.Service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser_Success() throws UserAlreadyExists {
        String username = "testUser";
        String password = "testPassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword");

        User createdUser = new User(username, "hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(createdUser);

        User result = userService.createUser(username, password);

        assertEquals(username, result.getUsername());
        assertEquals("hashedPassword", result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateUser_UserAlreadyExists() {
        String username = "existingUser";
        String password = "testPassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User(username, "existingPassword")));

        assertThrows(UserAlreadyExists.class, () -> userService.createUser(username, password));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testFindUserByUsername() {
        String username = "testUser";
        User user = new User(username, "testPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testFindUserById() {
        long userId = 9999L;
        User user = new User(userId,"testUser", "testPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testCheckUserCredentials_Success() throws UserDoesNotExist, UserWrongPassword {
        String username = "testUser";
        String password = "correctPassword";
        User user = new User(username, "hashedPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "hashedPassword")).thenReturn(true);

        UserDataDto userDataDto = new UserDataDto(username, password);

        assertDoesNotThrow(() -> userService.checkUserCredentials(userDataDto));
    }

    @Test
    public void testCheckUserCredentials_WrongPassword() {
        String username = "testUser";
        String password = "wrongPassword";
        User user = new User(username, "hashedPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "hashedPassword")).thenReturn(false);

        UserDataDto userDataDto = new UserDataDto(username, password);

        assertThrows(UserWrongPassword.class, () -> userService.checkUserCredentials(userDataDto));
    }

    @Test
    public void testGetAuthenticatedUser_Success() throws UserDoesNotExist {
        String username = "authenticatedUser";
        User user = new User(username, "hashedPassword");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        SecurityContextHolder.setContext(securityContext);

        User result = userService.getAuthenticatedUser();

        assertEquals(user, result);
    }

    @Test
    public void testGetAuthenticatedUser_UserDoesNotExist() {
        String username = "nonExistentUser";

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        SecurityContextHolder.setContext(securityContext);

        assertThrows(UserDoesNotExist.class, () -> userService.getAuthenticatedUser());
    }

    @Test
    public void testGetAuthenticatedUsername_AuthenticatedUser() {
        String username = "authenticatedUser";

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);

        assertEquals(username, userService.getAuthenticatedUsername());
    }
}
