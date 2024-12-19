package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.ErrorCode;

public class EmailSendFailException extends CustomException {

    public EmailSendFailException(String message) {
        super(ErrorCode.EMAIL_SEND_FAIL, message);
    }
}
