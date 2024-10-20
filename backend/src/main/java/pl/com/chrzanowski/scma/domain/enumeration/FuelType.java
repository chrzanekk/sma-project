package pl.com.chrzanowski.scma.domain.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum FuelType {
    BENZINE_PL("PETROL_PL", "Benzyna"),
    BENZINE_EN("PETROL_EN", "Petrol"),
    DIESEL_PL("DIESEL_PL", "OLEJ NAPĘDOWY"),
    DIESEL_EN("DIESEL_EN", "Diesel");


    private final String lang;
    private final String type;


    FuelType(String lang, String type) {
        this.lang = lang;
        this.type = type;

    }

    public String getType() {
        return type;
    }

    public String getLang() {
        return lang;
    }

    private static final Map<String, FuelType> fuelTypesWithCodes = new HashMap<>();

    static {
        for (FuelType fuelType : FuelType.values()) {
            fuelTypesWithCodes.put(fuelType.type, fuelType);
        }
    }

    public static FuelType findByType (String type) {
        return fuelTypesWithCodes.get(type);
    }
}
