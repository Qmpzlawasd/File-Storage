package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Modifying;
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
    @Query("SELECT f from File f JOIN Storage s ON f.fileId = s.file.fileId where f.fileName = :fileName and s.group.groupId = :groupId and s.file.isDeleted = false")
    Optional<File> findByFileNameAndGroupId(long groupId, String fileName);

    @Transactional(readOnly = true)
    @Query("SELECT f from File f JOIN Storage s ON f.fileId = s.file.fileId where f.fileId = :fileId and s.group.groupId = :groupId and s.file.isDeleted = false")
    Optional<File> findByFileIdAndGroupId(long groupId, long fileId);

    @Transactional(readOnly = true)
    @Modifying
    @Query("update File f set f.isDeleted = true where f.fileId = :fileId")
    void setFileToDeleted(long fileId);
}
