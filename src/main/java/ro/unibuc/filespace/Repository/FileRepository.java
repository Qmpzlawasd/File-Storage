package ro.unibuc.filespace.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.User;

@Repository
public interface FileRepository extends CrudRepository<User, Long> {
}
