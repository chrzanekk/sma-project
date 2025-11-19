package pl.com.chrzanowski.sma.common.exception.error;

public enum UnitErrorCode implements IErrorCode {
    UNIT_NOT_FOUND("unitNotFound"),
    SYMBOL_MISING("symbolMissing"),
    DELETE_NOT_POSSIBLE("deleteNotPossible");


    private final String code;

    UnitErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
