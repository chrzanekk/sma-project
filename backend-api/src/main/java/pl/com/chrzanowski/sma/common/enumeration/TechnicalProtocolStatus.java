package pl.com.chrzanowski.sma.common.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TechnicalProtocolStatus {
    TO_BE_CREATED("toBeCreated"),
    CREATED("created"),
    IN_SIGNATURE("inSignature"),
    IN_SIGNATURE_2("inSignature2"),
    RECEIVED("received");

    private String name;

    TechnicalProtocolStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
