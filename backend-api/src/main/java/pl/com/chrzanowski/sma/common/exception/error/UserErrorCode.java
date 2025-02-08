package pl.com.chrzanowski.sma.common.exception.error;

public enum UserErrorCode implements IErrorCode {
    USER_ADDED_ERROR("userAddedError"),
    USER_PASSWORD_SET_ERROR("userPasswordSetError"),
    USER_EDITED_ERROR("userEditedError"),
    USER_NOT_FOUND("userNotFound");

    private final String code;

    UserErrorCode(String code) {
        this.code = code;
    }


    @Override
    public String getCode() {
        return code;
    }
}
