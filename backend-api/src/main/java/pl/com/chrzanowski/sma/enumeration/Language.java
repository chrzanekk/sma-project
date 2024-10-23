package pl.com.chrzanowski.sma.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum Language {
    PL("pl_PL"),
    US("us_US"),
    EN("en_EN"),
    ENGLISH("en"),
    POLISH("pl");
    private String code;


    Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }



    private static final Map<String, Language> codesWithEnums;


    static {
        codesWithEnums = new HashMap<>();
        for (Language codeWithEnum : Language.values()) {
            codesWithEnums.put(codeWithEnum.code, codeWithEnum);
        }
    }



    public static Language from(String code) {
        return codesWithEnums.get(code);
    }
}
