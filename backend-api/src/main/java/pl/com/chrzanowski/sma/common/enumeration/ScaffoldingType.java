package pl.com.chrzanowski.sma.common.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ScaffoldingType {
    SUSPENDED("suspended"),
    MOBILE("mobile"),
    BASIC("basic");


    private final String type;

    ScaffoldingType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    public static ScaffoldingType fromType(String input) {
        for (ScaffoldingType type : values()) {
            if (type.type.equalsIgnoreCase(input)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ScaffoldingType: " + input);
    }
}
