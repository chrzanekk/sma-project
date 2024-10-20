package pl.com.chrzanowski.scma.auth.request;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private Boolean rememberMe;

    private LoginRequest(Builder builder) {
        setUsername(builder.username);
        setPassword(builder.password);
        setRememberMe(builder.rememberMe);
    }

    public LoginRequest() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(LoginRequest copy) {
        Builder builder = new Builder();
        builder.username = copy.getUsername();
        builder.password = copy.getPassword();
        builder.rememberMe = copy.isRememberMe();
        return builder;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isRememberMe() {
        return rememberMe;
    }

    public LoginRequest setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
        return this;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password=[HIDDEN]" +
                ", rememberMe=" + rememberMe +
                '}';
    }

    public static final class Builder {
        private @NotBlank String username;
        private @NotBlank String password;
        private Boolean rememberMe;

        private Builder() {
        }

        public Builder username(@NotBlank String username) {
            this.username = username;
            return this;
        }

        public Builder password(@NotBlank String password) {
            this.password = password;
            return this;
        }

        public Builder rememberMe(Boolean rememberMe) {
            this.rememberMe = rememberMe;
            return this;
        }

        public LoginRequest build() {
            return new LoginRequest(this);
        }
    }
}
