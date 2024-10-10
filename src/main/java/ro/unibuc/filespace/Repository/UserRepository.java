package ro.unibuc.filespace.Repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends  CrudRepository<User, Long>{
    Optional<User> findByUsername(String username);
}
