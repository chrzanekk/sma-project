package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.IErrorCode;

public class ContractException extends CustomException {

    public ContractException(IErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
