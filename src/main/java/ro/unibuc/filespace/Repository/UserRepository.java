package ro.unibuc.filespace.Repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.Group;
import ro.unibuc.filespace.Model.User;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends  CrudRepository<User, Long>{
    Optional<User> findByUsername(String username);

    @Query("SELECT g from User u JOIN Membership m ON m.userId = u.userId JOIN Group g on g.groupId =  m.groupId where u.userId = :id")
    List<Group> findUserGroups(Long id);

}
