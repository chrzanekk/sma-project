package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

public class UnitException extends CustomException {
    public UnitException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
