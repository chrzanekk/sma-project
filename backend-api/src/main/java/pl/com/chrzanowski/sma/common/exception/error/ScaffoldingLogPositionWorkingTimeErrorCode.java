package pl.com.chrzanowski.sma.common.exception.error;

public enum ScaffoldingLogPositionWorkingTimeErrorCode implements IErrorCode {
    SCAFFOLDING_LOG_POSITION_WORKING_TIME_NOT_FOUND("scaffoldingLogWorkingTimes.scaffoldingLogPositionWorkingTimeNotFound"),
    DELETE_NOT_POSSIBLE("scaffoldingLogWorkingTimes.deleteNotPossible");


    private final String code;

    ScaffoldingLogPositionWorkingTimeErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
