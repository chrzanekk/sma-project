package pl.com.chrzanowski.sma.common.exception.error;

public enum ErrorCode {
    EMAIL_NOT_FOUND("emailNotFound"),
    EMAIL_ALREADY_EXISTS("emailAlreadyExists"),
    EMAIL_SEND_FAIL("emailSendFail"),
    EXPIRED_TOKEN("expiredToken"),
    OBJECT_NOT_FOUND("objectNotFound"),
    PASSWORD_NOT_MATCH("passwordNotMatch"),
    USERNAME_EXISTS("usernameExists"),
    LOGIN_ALREADY_EXISTS("loginAlreadyExists"),
    INVALID_ARGUMENT("invalidArgument"),
    ROLE_NOT_FOUND("roleNotFound"),
    ROLE_CANNOT_BE_DELETED("roleCannotBeDeleted"),
    INVALID_PASSWORD("invalidPassword"),
    GENERIC_ERROR("genericError"),
    MALFORMED_JSON("malformedJson"),
    USER_ADDED_ERROR("userAddedError"),
    USER_PASSWORD_SET_ERROR("userPasswordSetError"),
    USER_EDITED_ERROR("userEditedError");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
