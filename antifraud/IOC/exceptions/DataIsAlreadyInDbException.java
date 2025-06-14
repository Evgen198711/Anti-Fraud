package antifraud.IOC.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DataIsAlreadyInDbException extends RuntimeException{
    public DataIsAlreadyInDbException(String s) {
        super(s);
    }
}
