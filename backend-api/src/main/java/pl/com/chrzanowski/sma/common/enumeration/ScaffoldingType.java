package pl.com.chrzanowski.sma.common.enumeration;

public enum ScaffoldingType {
    SUSPENDED("suspended"),
    MOBILE("mobile"),
    BASIC("basic");


    private final String type;

    ScaffoldingType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
