package ro.unibuc.filespace.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.filespace.Model.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Comment c SET c.comment = :content, c.updatedAt = CURRENT_TIMESTAMP WHERE c.commentId = :commentId AND c.commenterId = :userId")
    int updateCommentContent(
            @Param("commentId") Long commentId,
            @Param("userId") Long userId,
            @Param("content") String content);

    @Query("""
                SELECT c FROM Comment c
                JOIN File f ON c.fileId = f.fileId
                JOIN Storage s ON s.fileId = f.fileId
                WHERE s.groupId = :groupId
                  AND f.fileId = :fileId
            """)
    Page<Comment> getCommentThread(
            @Param("groupId") Long groupId,
            @Param("fileId") Long fileId,
            Pageable pageable
    );
}