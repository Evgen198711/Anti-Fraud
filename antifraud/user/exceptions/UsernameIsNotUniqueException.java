package antifraud.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Username is not unique!")
public class UsernameIsNotUniqueException extends RuntimeException{
}
