package pl.com.chrzanowski.sma.exception;

public class EmptyValueException extends RuntimeException {

    public EmptyValueException(String message) {
        super(message);
    }

    public EmptyValueException() {

    }
}
