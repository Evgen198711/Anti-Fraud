package antifraud.transaction.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DataAlreadySpecifiedException extends RuntimeException{
    public DataAlreadySpecifiedException(String message) {
        super(message);
    }
}
