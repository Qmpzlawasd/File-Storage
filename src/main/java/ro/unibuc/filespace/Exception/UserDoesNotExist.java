package ro.unibuc.filespace.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Account does not exist")
public class UserDoesNotExist extends RuntimeException {
}