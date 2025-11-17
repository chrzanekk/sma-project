package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

import java.util.Map;

public class ScaffoldingLogPositionException extends CustomException {

    public ScaffoldingLogPositionException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ScaffoldingLogPositionException(IErrorCode errorCode, String message, Map<String, Object> details) {
        super(errorCode, message, details);
    }
}
