package ro.unibuc.filespace.Model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StorageId implements Serializable {
    private Long groupId;
    private Long fileId;
    private Long userId;
}
