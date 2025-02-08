package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.AuthErrorCode;

import java.util.Map;

public class EmailNotFoundException extends CustomException {
    public EmailNotFoundException(String message, Map<String, Object> details) {
        super(AuthErrorCode.EMAIL_NOT_FOUND, message, details);
    }
}
