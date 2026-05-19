package pl.com.chrzanowski.sma.common.exception.error;

public enum UserErrorCode implements IErrorCode {
    USER_ADDED_ERROR("users.userAddedError"),
    USER_PASSWORD_SET_ERROR("users.userPasswordSetError"),
    USER_EDITED_ERROR("users.userEditedError"),
    USER_NOT_FOUND("users.userNotFound");

    private final String code;

    UserErrorCode(String code) {
        this.code = code;
    }


    @Override
    public String getCode() {
        return code;
    }
}
