package pl.com.chrzanowski.sma.common.exception.error;

public enum ContractErrorCode implements IErrorCode {
    CONTRACT_NOT_FOUND("contract.contractNotFound"),
    NUMBER_MISSING("contract.numberMissing"),
    VALUE_MISSING("contract.valueMissing"),
    DESCRIPTION_MISSING("contract.descriptionMissing"),
    START_DATE_MISSING("contract.startDateMissing"),
    END_DATE_MISSING("contract.endDateMissing"),
    SIGN_UP_DATE_MISSING("contract.signUpDateMissing");

    private final String code;

    ContractErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
