package ro.unibuc.filespace.Model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

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
    @Column(name = "comment_id", nullable = false, unique = true)
    private Long commentId;

    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "file_id", referencedColumnName = "file_id")
    private File file;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User commenter;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Comment(String comment, File file, User user,Long parentCommentId, LocalDateTime createdAt) {
        this.comment = comment;
        this.file = file;
        this.commenter = user;
        this.parentCommentId = parentCommentId;
        this.createdAt = createdAt;
    }

}
