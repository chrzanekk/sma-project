package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

import java.util.Map;

public class UsernameAlreadyExistsException extends CustomException {

    public UsernameAlreadyExistsException(String message, Map<String, Object> details) {
        super(ErrorCode.USERNAME_EXISTS, message, details);
    }
}
