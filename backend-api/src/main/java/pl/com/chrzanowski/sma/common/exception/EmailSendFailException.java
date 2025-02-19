package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.EmailSendErrorCode;

import java.util.Map;

public class EmailSendFailException extends CustomException {

    public EmailSendFailException(String message, Map<String, Object> details) {
        super(EmailSendErrorCode.EMAIL_SEND_FAIL, message, details);
    }
}
