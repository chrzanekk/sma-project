package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

public class UsernameAlreadyExistsException extends CustomException {

    public UsernameAlreadyExistsException(String message) {
        super(ErrorCode.USERNAME_EXISTS, message);
    }
}
