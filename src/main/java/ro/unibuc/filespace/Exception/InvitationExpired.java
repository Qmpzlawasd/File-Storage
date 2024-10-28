package ro.unibuc.filespace.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "invite expired")
public class InvitationExpired extends RuntimeException {
}
