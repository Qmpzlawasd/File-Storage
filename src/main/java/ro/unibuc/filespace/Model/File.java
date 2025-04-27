package ro.unibuc.filespace.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

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

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "file_content", nullable = false)
    private String fileContent;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public File(String file_name, User user, String file_content) {
        this.fileName = file_name;
        this.user = user;
        this.fileContent = file_content;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "files")
    private Set<Group> groups;
}
