package ro.unibuc.filespace.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

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
    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnore
    @JoinColumns({
            @JoinColumn(name = "file_id", referencedColumnName = "file_id"),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    })
    private File file;

    @Column(name = "extension")
    private String extension;

    @Column(name = "sizeBytes", nullable = false)
    private int sizeBytes;

    @Column(name = "crc32_checksum", nullable = false)
    private long crc32Checksum;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public FileMetadata(File file, String extension, int sizeBytes, long crc32Checksum) {
        this.file = file;
        this.extension = extension;
        this.sizeBytes = sizeBytes;
        this.crc32Checksum = crc32Checksum;
        this.createdAt = LocalDateTime.now();
    }
}
