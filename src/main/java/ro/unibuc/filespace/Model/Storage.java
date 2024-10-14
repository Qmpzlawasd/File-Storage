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
public class Storage {
    @Id
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "file_id", referencedColumnName = "file_id", nullable = false),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    })
    private File file;

}
