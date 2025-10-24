package pl.com.chrzanowski.sma.common.security.enums;

public enum ResourceKey {
    // Auth endpoints
    AUTH_PUBLIC("Authentication", ApiPath.AUTH + Constants.ALL, null, "Public authentication endpoints", true),

    // Account
    ACCOUNT_MANAGEMENT("Account", ApiPath.ACCOUNT + Constants.ALL, null, "User account management", false),

    // User Management
    USER_MANAGEMENT("Users", ApiPath.USER + Constants.ALL, null, "User administration", false),

    // Role Management
    ROLE_MANAGEMENT("Roles", ApiPath.ROLE + Constants.ALL, null, "Role administration", false),

    // Company Management
    COMPANY_MANAGEMENT("Companies", ApiPath.COMPANY + Constants.ALL, null, "Company management", false),

    // Contractor Management
    CONTRACTOR_MANAGEMENT("Contractors", ApiPath.CONTRACTOR + Constants.ALL, null, "Contractor management", false),

    // Contact Management
    CONTACT_MANAGEMENT("Contacts", ApiPath.CONTACT + Constants.ALL, null, "Contact management", false),

    // Construction Site Management
    CONSTRUCTION_SITE_MANAGEMENT("Construction Sites", ApiPath.CONSTRUCTION_SITE + Constants.ALL, null, "Construction site management", false),

    // Position Management
    POSITION_MANAGEMENT("Positions", ApiPath.POSITION + Constants.ALL, null, "Position management", false),

    // Contract Management
    CONTRACT_MANAGEMENT("Contracts", ApiPath.CONTRACT + Constants.ALL, null, "Contract management", false),

    // Resource Management (meta - zarządzanie samymi uprawnieniami)
    RESOURCE_MANAGEMENT("Resource Permissions", ApiPath.RESOURCE + Constants.ALL, null, "Resource permission management", false);

    private final String displayName;
    private final String endpointPattern;
    private final String httpMethod; // GET, POST, PUT, DELETE, null = all
    private final String description;
    private final boolean isPublic;

    ResourceKey(String displayName, String endpointPattern, String httpMethod, String description, boolean isPublic) {
        this.displayName = displayName;
        this.endpointPattern = endpointPattern;
        this.httpMethod = httpMethod;
        this.description = description;
        this.isPublic = isPublic;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEndpointPattern() {
        return endpointPattern;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    private static class Constants {
        public static final String ALL = "/**";
    }
}
