package pl.com.chrzanowski.sma.common.enumeration;

import lombok.Getter;

@Getter
public enum Country {
    POLAND("PL", "Polska"),
    ENGLAND("EN-GB", "Anglia");

    private final String code;
    private final String name;

    Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Country fromCode(String code) {
        for (Country country : values()) {
            if (country.getCode().equalsIgnoreCase(code)) {
                return country;
            }
        }
        throw new IllegalArgumentException("Unknown country code: " + code);
    }
}
