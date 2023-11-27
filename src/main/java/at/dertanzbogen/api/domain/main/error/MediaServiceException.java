package at.dertanzbogen.api.domain.main.error;

public class MediaServiceException extends RuntimeException
{
    public MediaServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}