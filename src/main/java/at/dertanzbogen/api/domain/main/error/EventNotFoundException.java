package at.dertanzbogen.api.domain.main.error;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventNotFoundException(Throwable cause) {
        super(cause);
    }

    public EventNotFoundException() {
        super();
    }

    public EventNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
