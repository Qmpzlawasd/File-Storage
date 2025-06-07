package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.*;

import java.util.List;

@Repository
public interface GroupTagRepository extends CrudRepository<GroupTag, GroupTagId> {
    @Query("SELECT t from GroupTag g, Tag t where g.groupId = :groupId and g.tagId = t.tagId")
    List<Tag> getTagsInGroup(long groupId);

    @Query("SELECT t from GroupTag g, Tag t where g.groupId = :groupId and g.fileId = :fileId and g.tagId = t.tagId")
    List<Tag> getFileTags(long groupId, long fileId);
}
