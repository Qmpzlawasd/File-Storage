package ro.unibuc.filespace.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "GROUPS")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long group_id;

    @Column(name = "group_name", nullable = false, unique = true)
    private String group_name;

    @ManyToMany
    @JoinTable(name = "STORAGE", joinColumns = {@JoinColumn(name = "group_id")}, inverseJoinColumns = {@JoinColumn(name = "file_id"), @JoinColumn(name = "user_id")})
    private Set<File> files;

    @ManyToMany
    @JoinTable(name = "MEMBERSHIP", joinColumns = {@JoinColumn(name = "group_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users;

}
