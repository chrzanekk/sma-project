package pl.com.chrzanowski.sma.common.exception.error;

public enum ResourceErrorCode implements IErrorCode {

    CRITICAL_RESOURCE_CANNOT_BE_DELETED("resources.criticalResourceCannotBeDeleted"),
    ONE_ROLE_NEEDED("resources.resourceNeedAtLeastOneRole");

    private final String code;

    ResourceErrorCode(String code) {
        this.code = code;
    }


    @Override
    public String getCode() {
        return code;
    }
}
