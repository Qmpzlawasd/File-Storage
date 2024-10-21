package ro.unibuc.filespace.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StorageId implements Serializable {
    private Group group;
    private File file;

}
