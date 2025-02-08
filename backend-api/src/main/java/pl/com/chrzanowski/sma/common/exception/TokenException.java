package pl.com.chrzanowski.sma.common.exception;

import pl.com.chrzanowski.sma.common.exception.error.TokenErrorCode;

public class TokenException extends CustomException {

    public TokenException(String message) {
        super(TokenErrorCode.TOKEN_MISSING, message);
    }
}
