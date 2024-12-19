package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

public class EmailAlreadyExistsException extends CustomException {

    public EmailAlreadyExistsException(String message) {
        super(ErrorCode.EMAIL_ALREADY_EXISTS, message);
    }
}
