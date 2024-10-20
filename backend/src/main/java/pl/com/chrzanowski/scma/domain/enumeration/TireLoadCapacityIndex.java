package pl.com.chrzanowski.scma.domain.enumeration;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TireLoadCapacityIndex {

    SEVENTY_FIVE("75",387),
    SEVENTY_SIX("76",400),
    SEVENTY_SEVEN("77",412),
    SEVENTY_EIGHT("78",425),
    SEVENTY_NINE("79",437),
    EIGHTY("80",450),
    EIGHTY_ONE("81",462),
    EIGHTY_TWO("82",475),
    EIGHTY_THREE("83",487),
    EIGHTY_FOUR("84",500),
    EIGHTY_FIVE("85",515),
    EIGHTY_SIX("86",530),
    EIGHTY_SEVEN("87",545),
    EIGHTY_EIGHT("88",569),
    EIGHTY_NINE("89",580),
    NINETY("90",600),
    NINETY_ONE("91",615),
    NINETY_TWO("92",630),
    NINETY_THREE("93",650),
    NINETY_FOUR("94",670),
    NINETY_FIVE("95",690),
    NINETY_SIX("96",710),
    NINETY_SEVEN("97",730),
    NINETY_EIGHT("98",750),
    NINETY_NINE("99",775),
    ONE_HUNDRED("100",800),
    ONE_HUNDRED_AND_ONE("101",825),
    ONE_HUNDRED_AND_TWO("102",850),
    ONE_HUNDRED_AND_THREE("103",875),
    ONE_HUNDRED_AND_FOUR("104",900),
    ONE_HUNDRED_AND_FIVE("105",925),
    ONE_HUNDRED_AND_SIX("106",950),
    ONE_HUNDRED_AND_SEVEN("107",975),
    ONE_HUNDRED_AND_EIGHT("108",1000),
    ONE_HUNDRED_AND_NINE("109",1030),
    ONE_HUNDRED_AND_TEN("110",1060);

    private final String index;
    private final Integer load;

    TireLoadCapacityIndex(String index, Integer load) {
        this.index = index;
        this.load = load;
    }

    public String getIndex() {
        return index;
    }

    public Integer getLoad() {
        return load;
    }

    public static final Map<String,Integer> LOAD_INDEXES = Stream.of(values()).collect(Collectors.toMap(k -> k.index,
            v -> v.load));
}
