package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
     Optional<Group> findByGroupId(long groupId);

     @Query("SELECT User from Membership where group.groupId = :groupId")
     List<User> getUsersInGroup(long groupId);

     @Query("SELECT User from Membership where group.groupId = :groupId and user.userId = :userId")
     Optional<User> findUserInGroup(long groupId, long userId);
}
