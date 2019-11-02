package space.meduzza.property.error;

public class MediaFileUploadException extends RuntimeException {

    public MediaFileUploadException() {
    }

    public MediaFileUploadException(String message) {
        super(message);
    }

    public MediaFileUploadException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }

    public MediaFileUploadException(Throwable cause) {
        super(cause);
    }

    public MediaFileUploadException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
