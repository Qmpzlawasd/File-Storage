package ro.unibuc.comment.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FileDTO  implements Serializable {
    private Long fileId;
    private Long userId;
    private String fileName;
    private String fileContent;
    private boolean isDeleted;
}