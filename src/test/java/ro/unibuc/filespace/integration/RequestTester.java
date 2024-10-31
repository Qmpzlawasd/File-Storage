package ro.unibuc.filespace.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import ro.unibuc.filespace.Dto.UserDataDto;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Service.FileService;
import ro.unibuc.filespace.Service.GroupService;
import ro.unibuc.filespace.Service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class RequestTester {
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nullable
    private User user;

    @Nullable
    private String jwtToken;

    public User createTestUser() {
        user = userService.createUser("user", "password");
        return user;
    }

    public String authenticateUser() throws Exception {
        assert user != null;
        var json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new UserDataDto(user.getUsername(),"password"));
        var request = post("/login").contentType(MediaType.APPLICATION_JSON).content(json);
        jwtToken = mockMvc.perform(request).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return jwtToken;
    }

    MockHttpServletRequestBuilder addTokenToRequest(MockHttpServletRequestBuilder request) {
        assert jwtToken != null;
        return request
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
    }

    public MockHttpServletRequestBuilder authenticatedPost(String url, Object body) throws JsonProcessingException {
        var json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
        return addTokenToRequest(post(url).content(json).contentType(MediaType.APPLICATION_JSON));
    }

    public MockHttpServletRequestBuilder Post(String url, Object body) throws JsonProcessingException {
        var json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
        return post(url).content(json).contentType(MediaType.APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder PostFile(String url, MultipartFile file) {
        return addTokenToRequest(MockMvcRequestBuilders.multipart(url)
                .file((MockMultipartFile) file)
                .contentType(MediaType.MULTIPART_FORM_DATA));
    }

    public MockHttpServletRequestBuilder authenticatedPatch(String url, Object body) throws JsonProcessingException {
        var json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
        return addTokenToRequest(patch(url).content(json).contentType(MediaType.APPLICATION_JSON));
    }

    public MockHttpServletRequestBuilder authenticatedGet(String url) {
        return addTokenToRequest(get(url));
    }
}
