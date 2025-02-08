package pl.com.chrzanowski.sma.common.exception.error;

public enum ObjectErrorCode implements IErrorCode {
    OBJECT_NOT_FOUND("objectNotFound");

    private final String code;

    ObjectErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
