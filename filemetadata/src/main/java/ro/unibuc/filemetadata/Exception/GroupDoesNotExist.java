package ro.unibuc.filemetadata.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Group Not Found")
public class GroupDoesNotExist extends RuntimeException {
}
