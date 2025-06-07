package ro.unibuc.filespace.Dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileResponseDTO {
    private String fileContentPreview;
    private String fileName;
    private String username;

    public FileResponseDTO(String fileContent, String fileName, String username) {
        this.fileName = fileName;
        this.username = username;
        this.fileContentPreview = fileContent != null && fileContent.length() > 100
                ? fileContent.substring(0, 100) + "...[truncated]"
                : fileContent;
    }
}
