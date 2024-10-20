package pl.com.chrzanowski.scma.exception;

public class EmailSendFailException extends RuntimeException {

    public EmailSendFailException(String message) {
        super(message);
    }

}
