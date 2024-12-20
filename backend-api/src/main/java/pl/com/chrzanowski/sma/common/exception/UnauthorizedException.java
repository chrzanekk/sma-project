package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

import java.util.Map;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(String message, Map<String, Object> details) {
        super(ErrorCode.UNAUTHORIZED, message, details);
    }

    public UnauthorizedException(String message) {
        this(message, null);
    }
}
