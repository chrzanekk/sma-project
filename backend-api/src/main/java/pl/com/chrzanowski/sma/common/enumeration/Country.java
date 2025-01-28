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

    public static Country getByType(String code) {
        return countriesWithCode.get(code);
    }

    @JsonValue
    public Map<String, String> toJson() {
        Map<String, String> jsonRepresentation = new HashMap<>();
        jsonRepresentation.put("code", code);
        jsonRepresentation.put("name", name);
        return jsonRepresentation;
    }
}
