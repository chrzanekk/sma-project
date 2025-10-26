package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

public class ResourceException extends CustomException {
    public ResourceException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
