package pl.com.chrzanowski.sma.common.exception.error;

public enum ScaffoldingLogPositionErrorCode implements IErrorCode {
    SCAFFOLDING_LOG_POSITION_NOT_FOUND("scaffoldingLogPositions.scaffoldingLogPositionNotFound"),
    SCAFFOLDING_LOG_POSITION_ALREADY_EXIST("scaffoldingLogPositions.scaffoldingLogPositionAlreadyExist"),
    DELETE_NOT_POSSIBLE("scaffoldingLogPositions.deleteNotPossible"),
    CANNOT_DELETE_HISTORICAL_POSITION("scaffoldingLogPositions.cannotDeleteHistoricalPosition"),
    INVALID_SCAFFOLDING_NUMBER_FORMAT("scaffoldingLogPositions.invalidScaffoldingNumberFormat"),
    INVALID_SCAFFOLDING_NUMBER_RANGE("scaffoldingLogPositions.invalidScaffoldingNumberRange"),
    INVALID_SCAFFOLDING_NUMBER_YEAR("scaffoldingLogPositions.invalidScaffoldingNumberYear"),
    INVALID_SCAFFOLDING_NUMBER_DATE("scaffoldingLogPositions.invalidScaffoldingNumberDate"),
    DUPLICATE_SCAFFOLDING_NUMBER("scaffoldingLogPositions.duplicateScaffoldingNumber"),
    ID_MISSING("scaffoldingLogPositions.idMissing");


    private final String code;

    ScaffoldingLogPositionErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
