package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

public class EmailNotFoundException extends CustomException {
    public EmailNotFoundException(String message) {
        super(ErrorCode.EMAIL_NOT_FOUND, message);
    }
}
