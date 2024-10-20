package pl.com.chrzanowski.scma.domain.enumeration;

public enum TireReinforcedIndex {

    XL("Zwiększona ładowność(XL)"),
    RF("Wzmocniana(RF)"),
    C("Dostawcza(C)"),
    SL("Standaradowa(SL)");

    private final String value;

    TireReinforcedIndex(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
