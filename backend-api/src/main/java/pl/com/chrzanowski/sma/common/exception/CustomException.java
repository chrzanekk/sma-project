package pl.com.chrzanowski.sma.common.exception;

import lombok.Getter;
import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

@Getter
public abstract class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
