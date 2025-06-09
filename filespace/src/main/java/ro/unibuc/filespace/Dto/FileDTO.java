package ro.unibuc.filespace.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO  implements Serializable {
    private Long fileId;
    private Long userId;
    private String fileName;
    private String fileContent;
    private boolean isDeleted;
}