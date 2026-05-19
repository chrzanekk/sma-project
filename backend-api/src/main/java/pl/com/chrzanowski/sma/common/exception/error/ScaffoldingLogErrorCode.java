package pl.com.chrzanowski.sma.common.exception.error;

public enum ScaffoldingLogErrorCode implements IErrorCode {
    SCAFFOLDING_LOG_NOT_FOUND("scaffoldingLogs.scaffoldingLogNotFound"),
    DELETE_NOT_POSSIBLE("scaffoldingLogs.deleteNotPossible");


    private final String code;

    ScaffoldingLogErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
