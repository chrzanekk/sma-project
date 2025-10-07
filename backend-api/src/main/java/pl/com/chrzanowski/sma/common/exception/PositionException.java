package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

public class PositionException extends CustomException {
    public PositionException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
