package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

public class WorkTypeException extends CustomException {
    public WorkTypeException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
