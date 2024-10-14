package ro.unibuc.filespace.Model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@IdClass(FileId.class)
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "file_id", nullable = false, unique = true)
    private Long fileId;

    @Column(name = "file_name", nullable = false, unique = true)
    private String file_name;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "file_content", nullable = false)
    private String file_content;

    public File(String file_name, User user, String file_content) {
        this.file_name = file_name;
        this.user = user;
        this.file_content = file_content;
    }

    @ManyToMany(mappedBy = "files")
    private Set<Group> groups;

}
