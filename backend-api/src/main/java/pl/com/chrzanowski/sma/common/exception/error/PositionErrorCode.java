package pl.com.chrzanowski.sma.common.exception.error;

public enum PositionErrorCode implements IErrorCode {
    POSITION_NOT_FOUND("positionNotFound"),
    NAME_MISING("nameMissing");


    private final String code;

    PositionErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
