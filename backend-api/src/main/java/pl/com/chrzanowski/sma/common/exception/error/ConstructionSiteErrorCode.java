package pl.com.chrzanowski.sma.common.exception.error;

public enum ConstructionSiteErrorCode implements IErrorCode {
    CONSTRUCTION_SITE_NOT_FOUND("constructionSiteNotFound");

    private final String code;

    ConstructionSiteErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
