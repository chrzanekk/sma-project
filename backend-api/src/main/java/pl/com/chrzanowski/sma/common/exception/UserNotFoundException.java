package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.UserErrorCode;

import java.util.Map;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String message, Map<String, Object> details) {
        super(UserErrorCode.USER_NOT_FOUND, message, details);
    }

    public UserNotFoundException(String message) {
        super(UserErrorCode.USER_NOT_FOUND, message);
    }
}
