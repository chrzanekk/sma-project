package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

import java.util.Map;

public class EmailSendFailException extends CustomException {

    public EmailSendFailException(String message, Map<String, Object> details) {
        super(ErrorCode.EMAIL_SEND_FAIL, message, details);
    }
}
