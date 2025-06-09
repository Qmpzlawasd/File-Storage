package ro.unibuc.comment.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Group name already used")
public class GroupAlreadyExists  extends RuntimeException{

}
