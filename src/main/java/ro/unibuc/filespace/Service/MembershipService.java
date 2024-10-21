package ro.unibuc.filespace.Service;

import jakarta.persistence.IdClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.filespace.Model.*;
import ro.unibuc.filespace.Repository.MembershipRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@IdClass(MembershipId.class)
public class MembershipService {
    private final MembershipRepository  membershipRepository;

    public void createMembership(Group group, User user) {
        membershipRepository.save(new Membership(group, user));
    }
}