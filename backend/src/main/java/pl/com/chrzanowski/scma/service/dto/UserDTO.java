package pl.com.chrzanowski.scma.service.dto;

import java.util.Set;

public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private String password;
    private Boolean locked;
    private Boolean enabled;
    private Set<RoleDTO> roles;

    public UserDTO() {
    }

    public UserDTO(Long id, String email, String username, String password, Boolean locked, Boolean enabled,
                   Set<RoleDTO> roles) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.locked = locked;
        this.enabled = enabled;
        this.roles = roles;
    }

    private UserDTO(Builder builder) {
        setId(builder.id);
        setEmail(builder.email);
        setUsername(builder.username);
        setPassword(builder.password);
        setLocked(builder.locked);
        setEnabled(builder.enabled);
        setRoles(builder.roles);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(UserDTO copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.email = copy.getEmail();
        builder.username = copy.getUsername();
        builder.password = copy.getPassword();
        builder.roles = copy.getRoles();
        return builder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isLocked=" + locked +
                ", isEnabled=" + enabled +
                ", roles=" + roles +
                '}';
    }


    public static final class Builder {
        private Long id;
        private String email;
        private String username;
        private String password;
        private Boolean locked;
        private Boolean enabled;
        private Set<RoleDTO> roles;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder locked(Boolean locked) {
            this.locked = locked;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder roles(Set<RoleDTO> roles) {
            this.roles = roles;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }
    }
}
