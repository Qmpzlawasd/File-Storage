
package ro.unibuc.filespace.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FileMetadataId implements Serializable {
    private File file;
}