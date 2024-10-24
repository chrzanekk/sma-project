package pl.com.chrzanowski.sma.common.exception;

public class EmailSendFailException extends RuntimeException {

    public EmailSendFailException(String message) {
        super(message);
    }

}
