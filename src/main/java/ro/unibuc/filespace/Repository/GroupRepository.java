package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Modifying;
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

     Optional<Group> findByGroupName(String GroupName);

     @Query("SELECT User from Membership where group.groupId = :groupId")
     List<User> getUsersInGroup(long groupId);

     @Query("SELECT m.user FROM Membership m WHERE m.group.groupId = :groupId AND m.user.userId = :userId")
     Optional<User> findUserInGroup(long groupId, long userId);
}