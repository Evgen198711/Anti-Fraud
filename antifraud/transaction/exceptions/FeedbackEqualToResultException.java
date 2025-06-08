package antifraud.transaction.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class FeedbackEqualToResultException extends RuntimeException{
    public FeedbackEqualToResultException(String message) {
        super(message);
    }
}
