package pl.com.chrzanowski.sma.common.exception.error;

public enum PositionErrorCode implements IErrorCode {
    POSITION_NOT_FOUND("positions.positionNotFound"),
    NAME_MISING("positions.nameMissing"),
    DELETE_NOT_POSSIBLE("positions.deleteNotPossible");


    private final String code;

    PositionErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
