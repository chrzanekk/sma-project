package pl.com.chrzanowski.sma.common.exception.error;

public enum ContactErrorCode implements IErrorCode {
    CONTACT_NOT_FOUND("contactNotFound"),
    FIRST_NAME_MISSING("firstNameMissing"),
    LAST_NAME_MISSING("lastNameMissing"),
    EMAIL_MISSING("emailMissing"),
    PHONE_NUMBER_MISSING("phoneNumberMissing");

    private final String code;

    ContactErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
