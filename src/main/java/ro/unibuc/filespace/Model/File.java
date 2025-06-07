package ro.unibuc.filespace.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "FILES")
@NoArgsConstructor
@IdClass(FileId.class)
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "file_id", nullable = false, unique = true)
    private Long fileId;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_content", length = 10000000, nullable = false)
    private String fileContent;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public File(String fileName, Long userId, String fileContent) {
        this.fileName = fileName;
        this.userId = userId;
        this.fileContent = fileContent;
    }
}
