package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

public class CompanyException extends CustomException {

    public CompanyException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
