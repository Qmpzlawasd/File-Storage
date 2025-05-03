package ro.unibuc.filespace.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupTagId implements Serializable {
    private Tag tag;
    private File file;
    private Group group;
}
