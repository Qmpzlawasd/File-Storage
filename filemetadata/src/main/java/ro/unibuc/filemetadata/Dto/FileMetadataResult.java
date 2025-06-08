package ro.unibuc.filemetadata.Dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Data
@NoArgsConstructor
public class FileMetadataResult {
    private String extension;
    private int sizeBytes;
    private long crc32Checksum;
}
