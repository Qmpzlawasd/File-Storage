package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.*;

import java.util.List;

@Repository
public interface StorageRepository extends CrudRepository<Storage, StorageId> {
    @Query("select f from Storage s , File f where s.groupId = :groupId and s.fileId = f.fileId and f.isDeleted = false")
    List<File> findFilesByGroup(Long groupId);
}
