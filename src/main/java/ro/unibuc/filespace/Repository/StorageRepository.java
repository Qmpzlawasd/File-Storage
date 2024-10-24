package ro.unibuc.filespace.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.FileId;
import ro.unibuc.filespace.Model.Storage;
import ro.unibuc.filespace.Model.StorageId;

@Repository
public interface StorageRepository extends CrudRepository<Storage, StorageId> {
}
