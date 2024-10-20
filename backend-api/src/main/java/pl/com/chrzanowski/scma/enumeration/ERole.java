package pl.com.chrzanowski.scma.enumeration;

public enum ERole {
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
