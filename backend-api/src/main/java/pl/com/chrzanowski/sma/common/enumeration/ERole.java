package pl.com.chrzanowski.sma.common.enumeration;

public enum ERole {
    /*
    Three basic roles, should be always present
     */
    ROLE_USER("ROLE_USER"),
    ROLE_MODERATOR("ROLE_MODERATOR"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String roleName;

    ERole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
