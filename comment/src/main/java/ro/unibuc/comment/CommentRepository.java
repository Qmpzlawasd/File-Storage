package ro.unibuc.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Comment c SET c.comment = :content, c.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE c.commentId = :commentId AND c.commenterId = :userId")
    int updateCommentContent(
            @Param("commentId") Long commentId,
            @Param("userId") Long userId,
            @Param("content") String content);

    // Get root comments (no parent) for a file
    Page<Comment> findByFileIdAndParentCommentIdIsNull(Long fileId, Pageable pageable);

    // Get replies to a specific comment
    List<Comment> findByParentCommentId(Long parentCommentId);

    // Get a comment only if it belongs to the specified file
    Optional<Comment> findByCommentIdAndFileId(Long commentId, Long fileId);

    // Check if comment exists in file
    boolean existsByCommentIdAndFileId(Long commentId, Long fileId);

    // Count replies for a comment
    long countByParentCommentId(Long parentCommentId);

    @Query("SELECT c FROM Comment c WHERE c.fileId = :fileId " +
            "ORDER BY CASE WHEN c.parentCommentId IS NULL THEN c.commentId ELSE c.parentCommentId END, " +
            "c.createdAt")
    Page<Comment> getCommentThread(@Param("fileId") Long fileId, Pageable pageable);
}