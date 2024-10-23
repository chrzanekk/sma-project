package pl.com.chrzanowski.sma.exception;

public class PasswordNotMatchException extends RuntimeException{

    public PasswordNotMatchException(String message) {
        super(message);
    }
}
