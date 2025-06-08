package antifraud.IOC.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class WrongFormatException extends RuntimeException{
    public WrongFormatException(String s) {
        super(s);
    }
}
