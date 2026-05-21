package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

public class ScaffoldingLogPositionDimensionException extends CustomException {
    public ScaffoldingLogPositionDimensionException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
