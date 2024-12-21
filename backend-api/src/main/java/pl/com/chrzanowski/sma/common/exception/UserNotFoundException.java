package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

import java.util.Map;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String message, Map<String, Object> details) {
        super(ErrorCode.USER_NOT_FOUND, message, details);
    }
}
