package at.dertanzbogen.api.domain.main.error;

public class PartnerRequestException extends RuntimeException {
    public PartnerRequestException(String message) {
        super(message);
    }

    public PartnerRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public PartnerRequestException(Throwable cause) {
        super(cause);
    }

    public PartnerRequestException() {
        super();
    }

    public PartnerRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
