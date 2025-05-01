package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.filespace.Model.FileMetadata;
import ro.unibuc.filespace.Model.FileMetadataId;

import java.util.Optional;

@Repository
public interface FileMetadataRepository extends CrudRepository<FileMetadata, FileMetadataId> {
    @Transactional(readOnly = true)
    @Query("SELECT f from File f JOIN FileMetadata s ON f.fileId = s.file.fileId where f.fileId = :fileId and f.isDeleted = false")
    Optional<FileMetadata> findFileMetadata(long fileId);
}
