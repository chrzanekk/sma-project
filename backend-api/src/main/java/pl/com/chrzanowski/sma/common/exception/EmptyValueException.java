package pl.com.chrzanowski.sma.common.exception;

public class EmptyValueException extends RuntimeException {

    public EmptyValueException(String message) {
        super(message);
    }

    public EmptyValueException() {

    }
}
