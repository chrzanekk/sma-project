package pl.com.chrzanowski.sma.common.exception.error;

public enum ExceptionHandlerErrorCode implements IErrorCode {
    INVALID_ARGUMENT("invalidArgument"),
    GENERIC_ERROR("genericError"),
    MALFORMED_JSON("malformedJson");

    private final String code;

    ExceptionHandlerErrorCode(String code) {
        this.code = code;
    }


    @Override
    public String getCode() {
        return code;
    }
}
