package at.dertanzbogen.api.domain.main.error;

public class EmailTakenException extends RuntimeException {
    public EmailTakenException(String message) {
        super(message);
    }

    public EmailTakenException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailTakenException(Throwable cause) {
        super(cause);
    }

    public EmailTakenException() {
        super();
    }

    public EmailTakenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

