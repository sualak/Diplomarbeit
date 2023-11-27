package at.dertanzbogen.api.domain.main.error;

public class CourseNotBookedException extends RuntimeException {
    public CourseNotBookedException(String message) {
        super(message);
    }

    public CourseNotBookedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CourseNotBookedException(Throwable cause) {
        super(cause);
    }

    public CourseNotBookedException() {
        super();
    }

    public CourseNotBookedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
