package ro.unibuc.filemetadata.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "File with name already exists")
public class FileWithNameAlreadyExists extends Exception {
}
