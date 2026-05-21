package pl.com.chrzanowski.sma.common.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ScaffoldingOperationType {

    ASSEMBLY("assembly"),
    DISMANTLING("dismantling");


    private final String operationType;
    ScaffoldingOperationType(String operationType) {
        this.operationType = operationType;
    }

    @JsonValue
    public String getOperationType() {
        return operationType;
    }
}
