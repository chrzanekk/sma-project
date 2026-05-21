package pl.com.chrzanowski.sma.common.exception.error;

public enum ContactErrorCode implements IErrorCode {
    CONTACT_NOT_FOUND("contacts.contactNotFound"),
    FIRST_NAME_MISSING("contacts.firstNameMissing"),
    LAST_NAME_MISSING("contacts.lastNameMissing"),
    EMAIL_MISSING("contacts.emailMissing"),
    PHONE_NUMBER_MISSING("contacts.phoneNumberMissing");

    private final String code;

    ContactErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
