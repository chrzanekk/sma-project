package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

public class ObjectNotFoundException extends CustomException {

    public ObjectNotFoundException(String message) {
        super(ErrorCode.OBJECT_NOT_FOUND, message);
    }
}
