package pl.com.chrzanowski.sma.common.exception.error;

public enum ScaffoldingLogPositionDimensionErrorCode implements IErrorCode {
    SCAFFOLDING_LOG_POSITION_DIMENSION_NOT_FOUND("scaffoldingLogs.scaffoldingLogPositionDimensionNotFound"),
    DELETE_NOT_POSSIBLE("scaffoldingLogs.deleteNotPossible");


    private final String code;

    ScaffoldingLogPositionDimensionErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
