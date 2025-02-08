package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;
import pl.com.chrzanowski.sma.common.exception.error.RoleErrorCode;

import java.util.Map;

public class RoleException extends CustomException {

    public RoleException(String message, Map<String, Object> details) {
        super(RoleErrorCode.ROLE_NOT_FOUND, message, details);
    }

    public RoleException(String message) {
        super(RoleErrorCode.ROLE_NOT_FOUND, message);
    }

    public RoleException(IErrorCode errorCode, String message, Map<String, Object> details) {
        super(errorCode, message, details);
    }
}
