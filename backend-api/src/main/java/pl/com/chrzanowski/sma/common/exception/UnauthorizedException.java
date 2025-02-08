package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.AuthErrorCode;

import java.util.Map;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(String message, Map<String, Object> details) {
        super(AuthErrorCode.UNAUTHORIZED, message, details);
    }
}
