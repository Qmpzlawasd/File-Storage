package ro.unibuc.filespace.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "File is empty")
public class FileIsEmpty extends Exception {
}
