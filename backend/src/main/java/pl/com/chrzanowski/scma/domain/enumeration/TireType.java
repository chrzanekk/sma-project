package pl.com.chrzanowski.scma.domain.enumeration;

public enum TireType {

    R("Radialna"),
    D("Diagonalna");

    private final String value;


    TireType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
