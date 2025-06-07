package ro.unibuc.filespace.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "metadata for file with name does not exist")
public class FileMetadataNotPresent extends Exception {
}
