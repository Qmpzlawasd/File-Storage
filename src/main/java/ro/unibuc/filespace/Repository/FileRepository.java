package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.FileId;

import java.util.Optional;

@Repository
public interface FileRepository extends CrudRepository<File, FileId> {
    @Query("SELECT File from File f JOIN Storage s ON f.fileId = s.file.fileId where f.file_name = :filename and s.group.groupId = :groupId")
    Optional<File> findByFileNameAndGroupId(long groupId, String fileName);
}
