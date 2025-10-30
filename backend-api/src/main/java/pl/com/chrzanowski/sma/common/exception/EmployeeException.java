package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

public class EmployeeException extends CustomException {

    public EmployeeException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
