package pl.com.chrzanowski.sma.common.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum Country {
    POLAND("PL", "Polska"),
    ENGLAND("EN-GB", "Anglia");

    private String code;
    private String name;

    Country(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private static final Map<String, Country> countriesWithCode = new HashMap<>();

    static {
        for (Country country : Country.values()) {
            countriesWithCode.put(country.code, country);
        }
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
