package ro.unibuc.filespace.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileMetadataResult {
    private String extension;
    private int sizeBytes;
    private long crc32Checksum;
}
