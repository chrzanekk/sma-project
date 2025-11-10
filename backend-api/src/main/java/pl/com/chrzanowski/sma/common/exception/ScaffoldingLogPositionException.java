package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

public class ScaffoldingLogPositionException extends CustomException {
    public ScaffoldingLogPositionException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
