package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

public class PasswordNotMatchException extends CustomException{

    public PasswordNotMatchException(String message) {
        super(ErrorCode.PASSWORD_NOT_MATCH, message);
    }
}
