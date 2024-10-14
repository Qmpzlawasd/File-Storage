package ro.unibuc.filespace.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "GROUPS")
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long groupId;

    @Column(name = "group_name", nullable = false, unique = true)
    private String group_name;

    @ManyToMany
    @JoinTable(name = "STORAGE", joinColumns = {@JoinColumn(name = "group_id")}, inverseJoinColumns = {@JoinColumn(name = "file_id"), @JoinColumn(name = "user_id")})
    private Set<File> files;

    @ManyToMany
    @JoinTable(name = "MEMBERSHIP", joinColumns = {@JoinColumn(name = "group_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users;

    public Group(String group_name) {
        this.group_name = group_name;
    }
}
