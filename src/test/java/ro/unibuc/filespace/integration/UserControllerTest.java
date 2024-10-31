package ro.unibuc.filespace.integration;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ro.unibuc.filespace.Dto.UserDataDto;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestTester requestTester;

    private User user;

    @BeforeEach
    public void setUp() throws Exception {
        user = requestTester.createTestUser();
        requestTester.authenticateUser();
    }

    @Test
    void creationRequest_usernameNotTaken_createsUser() throws Exception {
        var userDto = new UserDataDto("newUser", "newUserPass");

        mockMvc.perform(requestTester.Post("/users", userDto)).andExpect(status().isCreated());

        var createdUser = userRepository.findByUsername(userDto.getUsername());

        assertFalse(createdUser.isEmpty());
    }

    @Test
    void creationRequest_usernameTaken_createsUser() throws Exception {
        var user1Dto = new UserDataDto("newUser", "newUserPass1");
        mockMvc.perform(requestTester.Post("/users", user1Dto)).andExpect(status().isCreated());

        var createdUser = userRepository.findByUsername(user1Dto.getUsername());
        assertFalse(createdUser.isEmpty());

        var user2Dto = new UserDataDto("newUser", "newUserPass2");
        mockMvc.perform(requestTester.Post("/users", user2Dto)).andExpect(status().isBadRequest());
    }
}
