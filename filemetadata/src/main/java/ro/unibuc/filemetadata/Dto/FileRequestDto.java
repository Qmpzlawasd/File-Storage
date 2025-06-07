package ro.unibuc.filemetadata.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileRequestDto {
    private Long fileId;
    private Long userId;
    private String fileName;
    private String fileContent;
}
