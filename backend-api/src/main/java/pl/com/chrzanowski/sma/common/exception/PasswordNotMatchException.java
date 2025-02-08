package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.AuthErrorCode;

public class PasswordNotMatchException extends CustomException {

    public PasswordNotMatchException(String message) {
        super(AuthErrorCode.PASSWORD_NOT_MATCH, message);
    }
}
