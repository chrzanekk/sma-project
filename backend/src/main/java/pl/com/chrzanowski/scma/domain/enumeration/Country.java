package pl.com.chrzanowski.scma.domain.enumeration;

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
}
