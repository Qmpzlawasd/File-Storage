package ro.unibuc.filespace.unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ro.unibuc.filespace.Dto.InvitationDto;
import ro.unibuc.filespace.Exception.GroupAlreadyExists;
import ro.unibuc.filespace.Exception.GroupDoesNotExist;
import ro.unibuc.filespace.Exception.UserDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Helper.EncryptionHelper;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.User;
import ro.unibuc.filespace.Repository.GroupRepository;
import ro.unibuc.filespace.Service.GroupService;
import ro.unibuc.filespace.Service.MembershipService;
import ro.unibuc.filespace.Service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private EncryptionHelper encryptionHelper;

    @Mock
    private UserService userService;

    @Mock
    private MembershipService membershipService;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUserToGroup_Success() throws GroupDoesNotExist, UserDoesNotExist {
        User user = new User("user1", "password");
        Group group = new Group("group1");
        group.setGroupId(1L);
        user.setUserId(1L);

        when(groupRepository.findByGroupId(group.getGroupId())).thenReturn(Optional.of(group));
        when(userService.findUserById(user.getUserId())).thenReturn(Optional.of(user));
        when(groupRepository.findUserInGroup(group.getGroupId(), user.getUserId())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> groupService.addUserToGroup(user, group));
        verify(membershipService, times(1)).createMembership(group, user);
    }

    @Test
    public void testAddUserToGroup_GroupDoesNotExist() {
        User user = new User("user1", "password");
        Group group = new Group("group1");
        group.setGroupId(1L);

        when(groupRepository.findByGroupId(group.getGroupId())).thenReturn(Optional.empty());

        assertThrows(GroupDoesNotExist.class, () -> groupService.addUserToGroup(user, group));
    }


    @Test
    public void testCreateGroup_GroupAlreadyExists() {
        String groupName = "existingGroup";

        when(groupRepository.findByGroupName(groupName)).thenReturn(Optional.of(new Group(groupName)));

        assertThrows(GroupAlreadyExists.class, () -> groupService.createGroup(groupName));
    }

    @Test
    public void testAcceptInvitation_InvalidUser() throws Exception {
        String token = "invitationToken";
        InvitationDto invitationDto = new InvitationDto("testGroup", "differentUser");
        User authenticatedUser = new User("testUser", "password");

        when(encryptionHelper.decodeInvitation(token)).thenReturn(invitationDto);
        when(userService.getAuthenticatedUser()).thenReturn(authenticatedUser);

        assertThrows(RuntimeException.class, () -> groupService.acceptInvitation(token));
    }
}
