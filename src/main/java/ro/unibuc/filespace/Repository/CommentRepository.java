package ro.unibuc.filespace.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    @Query(value = """
        WITH RECURSIVE comment_thread AS (
            -- Base case: Get root comments for the file in the specified group
            SELECT c.*
            FROM comment c
            JOIN FILES f ON c.file_id = f.file_id
            JOIN storage s ON f.file_id = s.file_id
            WHERE f.file_id = :fileId
              AND s.group_id = :groupId
              AND f.is_deleted = false
              AND c.parent_comment_id IS NULL  -- Root comments
            UNION ALL
            -- Recursive case: Get all replies to root/parent comments
            SELECT child.*
            FROM comment child
            JOIN comment_thread parent 
              ON child.parent_comment_id = parent.comment_id
        )
        SELECT * FROM comment_thread
        ORDER BY created_at
        """, nativeQuery = true)
    List<Comment> getCommentThread(
            @Param("groupId") Long groupId,
            @Param("fileId") Long fileId
    );
}
