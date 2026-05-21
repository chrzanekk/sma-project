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
}
