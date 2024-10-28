package ro.unibuc.filespace;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Repository.FileRepository;
import ro.unibuc.filespace.Repository.GroupRepository;
import ro.unibuc.filespace.Repository.StorageRepository;
import ro.unibuc.filespace.Service.FileService;
import ro.unibuc.filespace.Service.GroupService;

import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestTester requestTester;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private FileRepository fileRepository;

    private User user;
    @Autowired
    private FileService fileService;

    @BeforeEach
    public void setUp() throws Exception {
        user = requestTester.createTestUser();
        requestTester.authenticateUser();
    }

    @Test
    void uploadFileRequest_invalidGroup_badRequest() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "nameoffile1", "application/octet-stream", "test file\n".getBytes());
        mockMvc.perform(requestTester.PostFile("/0/upload", multipartFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadFileRequest_emptyFile_badRequest() throws Exception {
        Group newGroup = groupRepository.save(new Group("group1"));

        MockMultipartFile multipartFile = new MockMultipartFile("file", "nameoffile1", "application/octet-stream", "".getBytes());
        mockMvc.perform(requestTester.PostFile(String.format("/%d/upload", newGroup.getGroupId()), multipartFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadFileRequest_userNotInGroup_badRequest() throws Exception {
        Group newGroup = groupRepository.save(new Group("group1"));

        MockMultipartFile multipartFile = new MockMultipartFile("file", "nameoffile1", "application/octet-stream", "test".getBytes());
        mockMvc.perform(requestTester.PostFile(String.format("/%d/upload", newGroup.getGroupId()), multipartFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadFileRequest_invalidFileName_badRequest() throws Exception {
        Group newGroup = groupRepository.save(new Group("group1"));
        groupService.addUserToGroup(this.user.getUserId(), newGroup.getGroupId());

        MockMultipartFile multipartFile1 = new MockMultipartFile("file", "nameoffile1", "application/octet-stream", "test1".getBytes());
        mockMvc.perform(requestTester.PostFile(String.format("/%d/upload", newGroup.getGroupId()), multipartFile1))
                .andExpect(status().isCreated());

        MockMultipartFile multipartFile2 = new MockMultipartFile("file", "nameoffile1", "application/octet-stream", "test2".getBytes());
        mockMvc.perform(requestTester.PostFile(String.format("/%d/upload", newGroup.getGroupId()), multipartFile2))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadFileRequest_validData_fileCreated() throws Exception {
        Group newGroup = groupRepository.save(new Group("group1"));
        groupService.addUserToGroup(this.user.getUserId(), newGroup.getGroupId());

        String fileName = "nameoffile1";
        MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, "application/octet-stream", "test1".getBytes());
        mockMvc.perform(requestTester.PostFile(String.format("/%d/upload", newGroup.getGroupId()), multipartFile))
                .andExpect(status().isCreated());

        assertFalse(fileRepository.findByFileNameAndGroupId(newGroup.getGroupId(), fileName).isEmpty());
    }
}
