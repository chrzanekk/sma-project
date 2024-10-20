package pl.com.chrzanowski.scma.domain.enumeration;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TireSpeedIndex {

    A1("A1", 5),
    A2("A2", 10),
    A3("A3", 15),
    A4("A4", 20),
    A5("A5", 25),
    A6("A6", 30),
    A7("A7", 35),
    A8("A8", 40),
    B("B", 50),
    C("C", 60),
    D("D", 65),
    E("E", 70),
    F("F", 80),
    G("G", 90),
    J("J", 100),
    K("K", 110),
    L("L", 120),
    M("M", 130),
    N("N", 140),
    P("P", 150),
    Q("Q", 160),
    R("R", 170),
    S("S", 180),
    T("T",190),
    U("U",200),
    H("H",210),
    V("V",240),
    W("W",270),
    Y("Y",300);



    private final String name;
    private final Integer speed;

    TireSpeedIndex(String name, Integer speed) {
        this.name = name;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public Integer getSpeed() {
        return speed;
    }

    public static final Map<String,Integer> SPEED_INDEXES = Stream.of(values()).collect(Collectors.toMap(k -> k.name, v -> v.speed));

}
