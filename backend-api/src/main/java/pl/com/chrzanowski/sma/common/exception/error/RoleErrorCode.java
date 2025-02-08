package pl.com.chrzanowski.sma.common.exception.error;

public enum RoleErrorCode implements IErrorCode {
    ROLE_NOT_FOUND("roleNotFound"),
    ROLE_CANNOT_BE_DELETED("roleCannotBeDeleted");

    private final String code;

    RoleErrorCode(String code) {
        this.code = code;
    }


    @Override
    public String getCode() {
        return code;
    }
}
