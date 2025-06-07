package ro.unibuc.filespace.Model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GroupTagId implements Serializable {
    private Long tagId;
    private Long fileId;
    private Long userId;
    private Long groupId;
}
