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
    @Column(name = "tag_id", nullable = false)
    private Long tagId;

    @Id
    @Column(name = "file_id", nullable = false)
    private Long fileId;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "groupId", nullable = false)
    private Long groupId;
}
