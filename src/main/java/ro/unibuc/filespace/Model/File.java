package ro.unibuc.filespace.Model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "FILES")
public class File {
    @Id
    @Column(name = "file_name", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String file_name;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "file_content", nullable = false)
    private String file_content;
}
