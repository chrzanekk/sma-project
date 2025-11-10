package pl.com.chrzanowski.sma.common.exception.error;

public enum ScaffoldingLogPositionErrorCode implements IErrorCode {
    SCAFFOLDING_LOG_POSITION_NOT_FOUND("scaffoldingLogPositionNotFound"),
    DELETE_NOT_POSSIBLE("deleteNotPossible");


    private final String code;

    ScaffoldingLogPositionErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
