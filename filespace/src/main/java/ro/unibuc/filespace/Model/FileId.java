package ro.unibuc.filespace.Model;

import java.io.Serializable;
import java.util.Objects;

public class FileId implements Serializable {
    private Long fileId;
    private Long userId;

    public FileId() {}

    public FileId(Long fileId, Long userId) {
        this.fileId = fileId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileId)) return false;
        FileId that = (FileId) o;
        return Objects.equals(fileId, that.fileId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, userId);
    }
}
