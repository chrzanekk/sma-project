package pl.com.chrzanowski.sma.common.exception.error;

public enum AuthErrorCode implements IErrorCode{
    UNAUTHORIZED("unauthorized"),
    FORBIDDEN("forbidden"),
    EXPIRED_TOKEN("expiredToken"),
    PASSWORD_NOT_MATCH("passwordNotMatch"),
    ACCOUNT_NOT_ACTIVE("accountNotActive"),
    EMAIL_NOT_FOUND("emailNotFound"),
    INVALID_PASSWORD("invalidPassword");

    private final String code;

    AuthErrorCode(String code) {
        this.code = code;
    }


    @Override
    public String getCode() {
        return code;
    }
}
