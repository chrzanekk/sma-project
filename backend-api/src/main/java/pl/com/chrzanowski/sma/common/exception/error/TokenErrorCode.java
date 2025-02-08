package pl.com.chrzanowski.sma.common.exception.error;

public enum TokenErrorCode implements IErrorCode {
    TOKEN_MISSING("tokenMissing");


    private final String code;

    TokenErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
