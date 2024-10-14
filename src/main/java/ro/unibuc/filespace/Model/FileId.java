package ro.unibuc.filespace.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ro.unibuc.filespace.Service.UserService;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FileId implements Serializable {
    private User user;
    private Long fileId;
}