package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

public class ContractorException extends CustomException {

    public ContractorException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
