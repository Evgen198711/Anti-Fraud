package antifraud.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Identical data Exception")
public class IdenticalDataException extends RuntimeException{
}
