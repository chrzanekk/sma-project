package pl.com.chrzanowski.sma.common.exception.error;

public enum WorkTypeErrorCode implements IErrorCode {
    WORK_TYPE_NOT_FOUND("workTypeNotFound"),
    NAME_MISING("nameMissing"),
    DELETE_NOT_POSSIBLE("deleteNotPossible");


    private final String code;

    WorkTypeErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
