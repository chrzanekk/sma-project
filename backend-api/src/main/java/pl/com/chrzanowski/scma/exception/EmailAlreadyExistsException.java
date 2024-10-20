package pl.com.chrzanowski.scma.exception;

public class EmailAlreadyExistsException extends RuntimeException{

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
