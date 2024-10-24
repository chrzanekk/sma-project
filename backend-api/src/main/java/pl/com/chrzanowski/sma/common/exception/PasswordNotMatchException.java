package pl.com.chrzanowski.sma.common.exception;

public class PasswordNotMatchException extends RuntimeException{

    public PasswordNotMatchException(String message) {
        super(message);
    }
}
