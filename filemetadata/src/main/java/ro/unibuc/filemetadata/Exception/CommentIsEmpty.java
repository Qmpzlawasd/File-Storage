package ro.unibuc.filemetadata.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Comment is empty")
public class CommentIsEmpty extends Exception {
}
