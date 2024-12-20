package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

import java.util.Map;

public class EmailAlreadyExistsException extends CustomException {

    public EmailAlreadyExistsException(String message, Map<String, Object> details) {
        super(ErrorCode.EMAIL_ALREADY_EXISTS, message, details);
    }
}
