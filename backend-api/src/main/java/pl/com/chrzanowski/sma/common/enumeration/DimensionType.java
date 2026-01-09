package pl.com.chrzanowski.sma.common.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DimensionType {
    CONSOLE("console"),
    SUSPENSION("suspension"),
    GIRDERS("girders"),
    BASIC_STRUCTURE("basicStructure");


    private final String type;

    DimensionType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }
}
