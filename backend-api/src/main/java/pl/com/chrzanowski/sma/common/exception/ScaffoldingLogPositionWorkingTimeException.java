package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

public class ScaffoldingLogPositionWorkingTimeException extends CustomException {
    public ScaffoldingLogPositionWorkingTimeException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
