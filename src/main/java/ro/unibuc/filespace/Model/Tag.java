package ro.unibuc.filespace.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "TAG")
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "tagId")
    private Long tagId;

    @Size(min = 1, max = 40, message = "Tag name cannot be shorter than 1 or longer than 40")
    @Column(name = "tagName", nullable = false)
    private String tagName;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "GROUP_TAG", joinColumns = {@JoinColumn(name = "tag_id")}, inverseJoinColumns = {
            @JoinColumn(name = "file_id", referencedColumnName = "file_id"),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "groupId", referencedColumnName = "groupId")
    })
    private Set<GroupTag> files = new HashSet<>();

    public Tag(String tagName, GroupTag groupTag) {
        this.tagName = tagName;
        this.files = Set.of(groupTag);
    }
}
