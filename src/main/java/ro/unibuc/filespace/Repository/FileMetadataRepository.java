package ro.unibuc.filespace.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.FileMetadata;
import ro.unibuc.filespace.Model.FileMetadataId;

@Repository
public interface FileMetadataRepository extends CrudRepository<FileMetadata, FileMetadataId> {

}
