package ro.unibuc.filespace.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "GROUPS")
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "groupId")
    private Long groupId;

    @Size(min = 2, max = 40, message = "Group name cannot be shorter than 2 or longer than 40")
    @Column(name = "groupName", nullable = false, unique = true)
    private String groupName;

    public Group(String group_name) {
        this.groupName = group_name;
    }
}
