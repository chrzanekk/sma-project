package pl.com.chrzanowski.sma.common.exception.error;

public enum ContractErrorCode implements IErrorCode {
    CONTRACT_NOT_FOUND("contractNotFound"),
    NUMBER_MISSING("numberMissing"),
    VALUE_MISSING("valueMissing"),
    DESCRIPTION_MISSING("descriptionMissing"),
    START_DATE_MISSING("startDateMissing"),
    END_DATE_MISSING("endDateMissing"),
    SIGN_UP_DATE_MISSING("signUpDateMissing");

    private final String code;

    ContractErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
