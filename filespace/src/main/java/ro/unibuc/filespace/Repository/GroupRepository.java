package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
     Optional<Group> findByGroupId(long groupId);

     Optional<Group> findByGroupName(String GroupName);

     @Query("SELECT u FROM Membership m , User u WHERE u.userId = m.userId and m.groupId = :groupId AND m.userId = :userId")
     Optional<User> findUserInGroup(long groupId, long userId);

     @Query("SELECT f FROM Storage s , File f WHERE s.fileId = f.fileId and s.groupId = :groupId and f.isDeleted = false")
     List<File> findFilesInGroup(long groupId);
}
