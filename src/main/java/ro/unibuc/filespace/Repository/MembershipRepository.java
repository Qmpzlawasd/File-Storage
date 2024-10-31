package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.filespace.Model.*;

import java.util.List;

@Repository
public interface MembershipRepository extends CrudRepository<Membership, MembershipId> {
    @Query("SELECT m.user from Membership m where m.group.groupId = :groupId")
    List<User> getUsersInGroup(long groupId);

    @Query("SELECT m.group from Membership m where m.user.userId = :userId")
    List<Group> getGroupsFromUser(long userId);
}