package pl.com.chrzanowski.sma.common.enumeration;

public enum ScaffoldingOperationType {

    ASSEMBLY("assembly"),
    DISMANTLING("dismantling");


    private String operationType;
    ScaffoldingOperationType(String operationType) {
        this.operationType = operationType;
    }
    public String getOperationType() {
        return operationType;
    }
}
