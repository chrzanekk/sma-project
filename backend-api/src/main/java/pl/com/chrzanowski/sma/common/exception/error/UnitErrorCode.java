package pl.com.chrzanowski.sma.common.exception.error;

public enum UnitErrorCode implements IErrorCode {
    UNIT_NOT_FOUND("unitNotFound"),
    SYMBOL_MISING("symbolMissing"),
    DELETE_NOT_POSSIBLE("deleteNotPossible"),
    GLOBAL_UNIT_CANNOT_BE_MODIFIED("globalUnitCannotBeModified"),
    GLOBAL_UNIT_CANNOT_BE_DELETED("globalUnitCannotBeDeleted");


    private final String code;

    UnitErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
