package pl.com.chrzanowski.sma.common.enumeration;

public enum TokenType {
    CONFIRMATION_TOKEN("CONFIRMATION_TOKEN"),
    PASSWORD_RESET_TOKEN("PASSWORD_RESET_TOKEN");

    private final String value;

    TokenType(String value) {
        this.value = value;
    }
}
