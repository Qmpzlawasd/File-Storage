package ro.unibuc.filespace.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JoinTable(name = "FILE_TAG", joinColumns = {@JoinColumn(name = "tag_id")}, inverseJoinColumns = {
            @JoinColumn(name = "file_id", referencedColumnName = "file_id"),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    })
    private Set<File> files;

    public Tag(String group_name) {
        this.tagName = group_name;
    }
}
