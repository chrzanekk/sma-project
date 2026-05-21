package pl.com.chrzanowski.sma.common.exception.error;

public enum UnitErrorCode implements IErrorCode {
    UNIT_NOT_FOUND("units.unitNotFound"),
    SYMBOL_MISING("units.symbolMissing"),
    DELETE_NOT_POSSIBLE("units.deleteNotPossible"),
    GLOBAL_UNIT_CANNOT_BE_MODIFIED("units.globalUnitCannotBeModified"),
    GLOBAL_UNIT_CANNOT_BE_DELETED("units.globalUnitCannotBeDeleted");


    private final String code;

    UnitErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
