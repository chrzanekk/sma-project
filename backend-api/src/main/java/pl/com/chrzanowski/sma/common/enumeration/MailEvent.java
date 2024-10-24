package pl.com.chrzanowski.sma.common.enumeration;

public enum MailEvent {
    AFTER_REGISTRATION("r"),
    AFTER_CONFIRMATION("c"),
    AFTER_PASSWORD_CHANGE("p"),
    PASSWORD_RESET("n"),
    EMAIL_CONFIRMATION_LINK("l");

    private String code;

    MailEvent(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
