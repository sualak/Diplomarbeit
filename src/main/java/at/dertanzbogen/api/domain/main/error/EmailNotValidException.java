package at.dertanzbogen.api.domain.main.error;

public class EmailNotValidException extends RuntimeException {
    public EmailNotValidException(String message) {
        super(message);
    }

    public EmailNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotValidException(Throwable cause) {
        super(cause);
    }

    public EmailNotValidException() {
        super();
    }

    public EmailNotValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
