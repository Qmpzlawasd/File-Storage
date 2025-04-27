
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
@Table(name = "FILE_TAGS")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@IdClass(FileTagId.class)
public class FileTags {
    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumns({
            @JoinColumn(name = "file_id", referencedColumnName = "file_id"),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    })
    private File file;

}

