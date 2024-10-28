package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.filespace.Model.File;
import ro.unibuc.filespace.Model.FileId;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends CrudRepository<File, FileId> {
    @Transactional(readOnly = true)
    @Query("SELECT f from File f JOIN Storage s ON f.fileId = s.file.fileId where f.file_name = :fileName and s.group.groupId = :groupId")
    Optional<File> findByFileNameAndGroupId(long groupId, String fileName);
}
