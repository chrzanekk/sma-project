package pl.com.chrzanowski.sma.common.exception.error;

public enum RegisterErrorCode implements IErrorCode {
    EMAIL_ALREADY_EXISTS("emailAlreadyExists"),
    USERNAME_EXISTS("usernameExists"),
    LOGIN_ALREADY_EXISTS("loginAlreadyExists");

    private final String code;

    RegisterErrorCode(String code) {
        this.code = code;
    }


    @Override
    public String getCode() {
        return code;
    }
}

