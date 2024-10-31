package ro.unibuc.filespace.Service;

import jakarta.persistence.IdClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.filespace.Exception.GroupDoesNotExist;
import ro.unibuc.filespace.Exception.UserDoesNotExist;
import ro.unibuc.filespace.Exception.UserNotInGroup;
import ro.unibuc.filespace.Model.*;
import ro.unibuc.filespace.Repository.MembershipRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@IdClass(MembershipId.class)
public class MembershipService {
    private final MembershipRepository  membershipRepository;

    public void createMembership(Group group, User user) {
        membershipRepository.save(new Membership(group, user));
    }

    public List<User> getUsersInGroup(long groupId) throws UserNotInGroup {
        return membershipRepository.getUsersInGroup(groupId);
    }

    public List<Group> getJoinedGroups(User user) throws UserDoesNotExist {
        return membershipRepository.getGroupsFromUser(user.getUserId());
    }
}
