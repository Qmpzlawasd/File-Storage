
package ro.unibuc.filespace.Model;
import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Setter
@Entity
@Table(name = "GROUP_TAG")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@IdClass(GroupTagId.class)
public class GroupTag {
    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumns({
            @JoinColumn(name = "file_id", referencedColumnName = "file_id", nullable = false),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    })
    private File file;

    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "groupId", nullable = false)
    private Group group;

    public GroupTag(File file, Group group) {
        this.file = file;
        this.group = group;
    }
}

