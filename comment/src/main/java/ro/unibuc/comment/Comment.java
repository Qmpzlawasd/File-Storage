package ro.unibuc.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "COMMENT")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "comment_id", nullable = false, updatable = false)
    private Long commentId;

    @Column(name = "file_id", nullable = false, updatable = false)
    private Long fileId;

    @Column(name = "file_user_id", nullable = false, updatable = false)
    private Long fileUserId;

    @Column(name = "commenter_id", nullable = false, updatable = false)
    private Long commenterId;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "parent_comment_id", updatable = false)
    private Long parentCommentId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", updatable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime updatedAt = null;

    public Comment(String comment, Long fileId, Long fileUserId, Long commenterId, Long parentCommentId, LocalDateTime createdAt) {
        this.comment = comment;
        this.fileId = fileId;
        this.fileUserId = fileUserId;
        this.commenterId = commenterId;
        this.parentCommentId = parentCommentId;
        this.createdAt = createdAt;
    }

    public Comment(String comment, Long fileId, Long fileUserId, Long commenterId, LocalDateTime createdAt) {
        this.comment = comment;
        this.fileId = fileId;
        this.fileUserId = fileUserId;
        this.commenterId = commenterId;
        this.createdAt = createdAt;
    }
}
