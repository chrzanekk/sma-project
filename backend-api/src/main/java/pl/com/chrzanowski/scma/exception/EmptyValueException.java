package pl.com.chrzanowski.scma.exception;

public class EmptyValueException extends RuntimeException {

    public EmptyValueException(String message) {
        super(message);
    }

    public EmptyValueException() {

    }
}
