package pl.com.chrzanowski.sma.common.security.enums;

import pl.com.chrzanowski.sma.common.enumeration.ICode;

public enum ResourceCode implements ICode {

    AUTHENTICATION("authentication", "authenticationManagement"),
    ACCOUNT("account","accountManagement"),
    USER("user","userManagement"),
    ROLE("role","roleManagement"),
    COMPANY("company","companyManagement"),
    CONTRACTOR("contractor","contractorManagement"),
    CONTACT("contact","contactManagement"),
    CONTRACT("contract","contractManagement"),
    CONSTRUCTION_SITE("constructionSite","constructionSiteManagement"),
    POSITION("position","positionManagement"),
    RESOURCE("resource","resourceManagement");


    private final String displayName;
    private final String description;

    ResourceCode(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }


    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
