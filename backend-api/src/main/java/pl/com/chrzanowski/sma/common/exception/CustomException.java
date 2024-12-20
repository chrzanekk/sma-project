package pl.com.chrzanowski.sma.common.exception;

import lombok.Getter;
import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

import java.util.Map;

@Getter
public abstract class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public CustomException(ErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }

    public CustomException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }
}
