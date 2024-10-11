package ro.unibuc.filespace.Model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "FILES")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long file_id;

    @Column(name = "file_name", nullable = false, unique = true)
    private String file_name;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "file_content", nullable = false)
    private String file_content;

    @ManyToMany(mappedBy = "files")
    private Set<Group> groups;

}
