package pl.com.chrzanowski.sma.user.service.filter;

public class UserFilter {

    private Long id;
    private String emailStartsWith;
    private String usernameStartsWith;
    private Boolean isLocked;
    private Boolean isEnabled;

    public UserFilter() {
    }

    public Long getId() {
        return id;
    }

    public UserFilter setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEmailStartsWith() {
        return emailStartsWith;
    }

    public UserFilter setEmailStartsWith(String emailStartsWith) {
        this.emailStartsWith = emailStartsWith;
        return this;
    }

    public String getUsernameStartsWith() {
        return usernameStartsWith;
    }

    public UserFilter setUsernameStartsWith(String usernameStartsWith) {
        this.usernameStartsWith = usernameStartsWith;
        return this;
    }

    public Boolean isLocked() {
        return isLocked;
    }

    public UserFilter setLocked(Boolean locked) {
        isLocked = locked;
        return this;
    }

    public Boolean isEnabled() {
        return isEnabled;
    }

    public UserFilter setEnabled(Boolean enabled) {
        isEnabled = enabled;
        return this;
    }
}
