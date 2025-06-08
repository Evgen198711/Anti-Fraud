package antifraud.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Administrator cannot be deleted!")
public class AdminDeleteException extends RuntimeException{
}
