package at.dertanzbogen.api.domain.main.error;

public class DrinkNotFoundException extends RuntimeException {
    public DrinkNotFoundException(String message) {
        super(message);
    }

    public DrinkNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DrinkNotFoundException(Throwable cause) {
        super(cause);
    }

    public DrinkNotFoundException() {
        super();
    }

    public DrinkNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
