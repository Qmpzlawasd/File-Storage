package ro.unibuc.comment.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "File with name does not exist")
public class FileDoesNotExist extends Exception {
}
