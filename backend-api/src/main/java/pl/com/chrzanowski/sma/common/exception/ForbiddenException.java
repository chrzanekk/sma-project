package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.AuthErrorCode;

import java.util.Map;

public class ForbiddenException extends CustomException {
    public ForbiddenException(String message, Map<String, Object> details) {
        super(AuthErrorCode.FORBIDDEN, message, details);
    }
}
