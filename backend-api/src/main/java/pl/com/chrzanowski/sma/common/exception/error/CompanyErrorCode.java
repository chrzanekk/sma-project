package pl.com.chrzanowski.sma.common.exception.error;

public enum CompanyErrorCode implements IErrorCode {
    COMPANY_NOT_FOUND("companyNotFound"),
    NAME_MISSING("nameMissing");

    private final String code;

    CompanyErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
