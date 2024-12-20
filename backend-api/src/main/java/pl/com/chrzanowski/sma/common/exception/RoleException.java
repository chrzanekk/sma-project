package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

import java.util.Map;

public class RoleException extends CustomException {

    public RoleException(String message, Map<String, Object> details) {
        super(ErrorCode.ROLE_NOT_FOUND, message, details);
    }

    public RoleException(String message) {
        super(ErrorCode.ROLE_NOT_FOUND, message);
    }

    public RoleException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(errorCode, message, details);
    }
}
