package pl.com.chrzanowski.scma.domain.enumeration;

public enum TireStatus {
    MOUNTED("m", "W użytku"),
    STOKED("s", "W magazynie"),
    DISPOSED("d", "Zutylizowane");

    private final String code;
    private final String description;

    TireStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
