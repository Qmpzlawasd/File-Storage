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
    @Query("SELECT u from Membership m, User u where u.userId = m.userId  and m.groupId = :groupId")
    List<User> getUsersInGroup(long groupId);

    @Query("SELECT g from Membership m , Group g where g.groupId = m.groupId and  m.userId = :userId")
    List<Group> getGroupsFromUser(long userId);
}