package ro.unibuc.filespace.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "groupId")
    private Long groupId;

    @Column(name = "groupName", nullable = false, unique = true)
    private String groupName;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "STORAGE", joinColumns = {@JoinColumn(name = "group_id")}, inverseJoinColumns = {@JoinColumn(name = "file_id"), @JoinColumn(name = "user_id")})
    private Set<File> files;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "MEMBERSHIP", joinColumns = {@JoinColumn(name = "group_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users;

    public Group(String group_name) {
        this.groupName = group_name;
    }
}
