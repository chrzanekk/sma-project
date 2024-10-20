package pl.com.chrzanowski.scma.domain.enumeration;

public enum TireSeasonType {
    WINTER("winter"),
    SUMMER("summer"),
    ALL_SEASON("all_seasons");
    private final String code;

    TireSeasonType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
