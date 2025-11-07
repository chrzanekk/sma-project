package pl.com.chrzanowski.sma.common.enumeration;

public enum DimensionType {
    CONSOLE("console"),
    SUSPENSION("suspension"),
    GIRDERS("girders"),
    BASIC_STRUCTURE("basicStructure");


    private final String type;

    DimensionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
