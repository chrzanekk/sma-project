package pl.com.chrzanowski.sma.common.exception.error;

public enum ScaffoldingLogPositionErrorCode implements IErrorCode {
    SCAFFOLDING_LOG_POSITION_NOT_FOUND("scaffoldingLogPositionNotFound"),
    SCAFFOLDING_LOG_POSITION_ALREADY_EXIST("scaffoldingLogPositionAlreadyExist"),
    DELETE_NOT_POSSIBLE("deleteNotPossible"),
    CANNOT_DELETE_HISTORICAL_POSITION("cannotDeleteHistoricalPosition"),
    INVALID_SCAFFOLDING_NUMBER_FORMAT("invalidScaffoldingNumberFormat"),
    INVALID_SCAFFOLDING_NUMBER_RANGE("invalidScaffoldingNumberRange"),
    INVALID_SCAFFOLDING_NUMBER_YEAR("invalidScaffoldingNumberYear"),
    INVALID_SCAFFOLDING_NUMBER_DATE("invalidScaffoldingNumberDate"),
    DUPLICATE_SCAFFOLDING_NUMBER("duplicateScaffoldingNumber"),
    ID_MISSING("idMissing");


    private final String code;

    ScaffoldingLogPositionErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
