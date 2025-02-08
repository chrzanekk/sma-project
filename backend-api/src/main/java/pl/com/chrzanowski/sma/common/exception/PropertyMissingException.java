package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

import java.util.Map;

public class PropertyMissingException extends CustomException {
    public PropertyMissingException(IErrorCode errorCode, String message, Map<String, Object> details) {
        super(errorCode, message, details);
    }

    public PropertyMissingException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
