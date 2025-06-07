package ro.unibuc.filespace.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "FILE_METADATA")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(FileMetadataId.class)
public class FileMetadata {

    @Id
    @Column(name = "file_id", nullable = false, updatable = false)
    private Long fileId;

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "extension")
    private String extension;

    @Column(name = "sizeBytes", nullable = false)
    private int sizeBytes;

    @Column(name = "crc32_checksum", nullable = false)
    private long crc32Checksum;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public FileMetadata(Long fileId, Long userId, String extension, int sizeBytes, long crc32Checksum) {
        this.fileId = fileId;
        this.userId = userId;
        this.extension = extension;
        this.sizeBytes = sizeBytes;
        this.crc32Checksum = crc32Checksum;
        this.createdAt = LocalDateTime.now();
    }
}
