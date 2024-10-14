package ro.unibuc.filespace.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User does not belong to this group")
public class UserNotInGroup extends Exception{
}
