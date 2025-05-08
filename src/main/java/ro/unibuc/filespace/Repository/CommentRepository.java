package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    @Query(value ="""
            SELECT c FROM Comment c
            WHERE c.FILE_ID = :fileId
            AND c.FILE_ID IN (
            SELECT s.FILE_ID FROM Storage s
            WHERE s.GROUP_ID = :groupId
    )
    ORDER BY
    CASE WHEN c.PARENT_COMMENT_ID IS NULL THEN c.COMMENT_ID ELSE c.PARENT_COMMENT_ID END,
    c.CREATED_AT 
       """,nativeQuery=true)
    List<Comment> getCommentThread(
            @Param("groupId") Long groupId,
            @Param("fileId") Long fileId
    );
}
