package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

public class RoleException extends CustomException {

    public RoleException(String message) {
        super(ErrorCode.ROLE_NOT_FOUND, message);
    }
    public RoleException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
