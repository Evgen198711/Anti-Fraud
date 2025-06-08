package antifraud.user.api;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

@RestControllerAdvice
public class UserExceptionController {

    @ExceptionHandler
    public void handleUsernameNotFoundException(UsernameNotFoundException exception, ServletWebRequest request)
            throws IOException {
        assert request.getResponse() != null;
        request.getResponse().sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }
}
