package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

public class ConstructionSiteException extends CustomException {

    public ConstructionSiteException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
