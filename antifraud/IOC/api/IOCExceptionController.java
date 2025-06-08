package antifraud.IOC.api;

import antifraud.IOC.exceptions.DataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

@RestControllerAdvice
public class IOCExceptionController {

    @ExceptionHandler
    public void handleDataNotFoundException(DataNotFoundException exception, ServletWebRequest request)
            throws IOException {
        assert request.getResponse() != null;
        request.getResponse().sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }
}
