package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

public class ScaffoldingLogException extends CustomException {
    public ScaffoldingLogException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
