package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.Tag;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
}
