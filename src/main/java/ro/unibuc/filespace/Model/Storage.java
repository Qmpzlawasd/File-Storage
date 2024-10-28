package ro.unibuc.filespace.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "STORAGE")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(StorageId.class)
public class Storage {
    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumns({
            @JoinColumn(name = "file_id", referencedColumnName = "file_id", nullable = false),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    })
    private File file;
}
