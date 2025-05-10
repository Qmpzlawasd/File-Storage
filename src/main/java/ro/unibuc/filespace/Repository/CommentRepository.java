package ro.unibuc.filespace.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.unibuc.filespace.Model.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    @Query("""
                SELECT c FROM Comment c
                JOIN File f ON c.file.fileId = f.fileId
                JOIN Storage s ON s.file = f
                WHERE s.group.groupId = :groupId
                  AND f.fileId = :fileId
            """)
    Page<Comment> getCommentThread(
            @Param("groupId") Long groupId,
            @Param("fileId") Long fileId,
            Pageable pageable
    );
}