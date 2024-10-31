package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.*;

import java.util.List;

@Repository
public interface StorageRepository extends CrudRepository<Storage, StorageId> {
    @Query("select s.file from Storage s where s.group = :group and s.file.isDeleted = false")
    List<File> findFilesByGroup(Group group);
}
