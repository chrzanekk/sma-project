package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

public class ContactException extends CustomException {

    public ContactException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
