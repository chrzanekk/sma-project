package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.AuthErrorCode;

import java.util.Map;

public class AccountException extends CustomException {
    public AccountException(String message, Map<String, Object> details) {
        super(AuthErrorCode.ACCOUNT_NOT_ACTIVE, message, details);
    }
}
