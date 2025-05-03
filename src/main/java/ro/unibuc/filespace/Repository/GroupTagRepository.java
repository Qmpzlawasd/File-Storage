package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.*;

import java.util.List;

@Repository
public interface GroupTagRepository extends CrudRepository<GroupTag, GroupTagId> {
    @Query("SELECT g.tag from GroupTag g where g.group.groupId = :groupId")
    List<Tag> getTagsInGroup(long groupId);

    @Query("SELECT g.tag from GroupTag g where g.group.groupId = :groupId and g.file.fileId = :fileId")
    List<Tag> getFileTags(long groupId, long fileId);
}
