
package ro.unibuc.comment.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Incorrect password")
public class UserWrongPassword extends RuntimeException {
}