package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ObjectErrorCode;

public class ObjectNotFoundException extends CustomException {

    public ObjectNotFoundException(String message) {
        super(ObjectErrorCode.OBJECT_NOT_FOUND, message);
    }
}
