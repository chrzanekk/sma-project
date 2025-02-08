package pl.com.chrzanowski.sma.common.exception;

import lombok.Getter;
import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

import java.util.Map;

@Getter
public abstract class CustomException extends RuntimeException {
    private final IErrorCode errorCode;
    private final Map<String, Object> details;

    public CustomException(IErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }

    public CustomException(IErrorCode errorCode, String message, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }
}
