package at.dertanzbogen.api.domain.main.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMassage> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {
        ErrorMassage massage =new ErrorMassage(HttpStatus.NOT_FOUND, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(massage);
    }

    @ExceptionHandler(EmailTakenException.class)
    public ResponseEntity<ErrorMassage> handleEmailTakenException(EmailTakenException exception, WebRequest request) {
        ErrorMassage massage =new ErrorMassage(HttpStatus.CONFLICT, exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(massage);
    }
}
