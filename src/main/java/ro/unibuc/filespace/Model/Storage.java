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
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Id
    @Column(name = "file_id", nullable = false)
    private Long fileId;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;
}
