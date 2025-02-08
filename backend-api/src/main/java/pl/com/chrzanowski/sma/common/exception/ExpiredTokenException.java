package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.AuthErrorCode;

public class ExpiredTokenException extends CustomException {
    public ExpiredTokenException(String message) {
        super(AuthErrorCode.EXPIRED_TOKEN, message);
    }
}
