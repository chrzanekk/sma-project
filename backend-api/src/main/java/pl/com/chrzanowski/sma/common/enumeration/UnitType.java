package pl.com.chrzanowski.sma.common.enumeration;

public enum UnitType {
    LENGTH("length"),           // mb, km
    AREA("area"),              // m2
    VOLUME("volume"),          // m3
    DURATION("duration"),      // r-h (roboczogodzina)
    SPEED("speed"),            // k/h
    DENSITY("density"),        // m2/doba, m3/doba, mb/doba
    FORCE("force");            // kN/m

    private final String type;

    UnitType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
