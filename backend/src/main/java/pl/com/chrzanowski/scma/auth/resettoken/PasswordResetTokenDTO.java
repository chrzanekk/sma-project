package pl.com.chrzanowski.scma.auth.resettoken;

import java.time.LocalDateTime;
import java.util.Objects;

public class PasswordResetTokenDTO {

    private final Long id;
    private final String passwordResetToken;
    private final Long userId;
    private final String userName;
    private final String email;
    private final LocalDateTime createDate;
    private final LocalDateTime expireDate;
    private final LocalDateTime confirmDate;


    public PasswordResetTokenDTO(Long id,
                                 String passwordResetToken,
                                 Long userId,
                                 String userName,
                                 String email,
                                 LocalDateTime createDate,
                                 LocalDateTime expireDate,
                                 LocalDateTime confirmDate) {
        this.id = id;
        this.passwordResetToken = passwordResetToken;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.createDate = createDate;
        this.expireDate = expireDate;
        this.confirmDate = confirmDate;
    }

    private PasswordResetTokenDTO(Builder builder) {
        id = builder.id;
        passwordResetToken = builder.passwordResetToken;
        userId = builder.userId;
        userName = builder.userName;
        email = builder.email;
        createDate = builder.createDate;
        expireDate = builder.expireDate;
        confirmDate = builder.confirmDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(PasswordResetTokenDTO copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.passwordResetToken = copy.getPasswordResetToken();
        builder.userId = copy.getUserId();
        builder.userName = copy.getUserName();
        builder.email = copy.getEmail();
        builder.createDate = copy.getCreateDate();
        builder.expireDate = copy.getExpireDate();
        builder.confirmDate = copy.getConfirmDate();
        return builder;
    }

    public Long getId() {
        return id;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public LocalDateTime getConfirmDate() {
        return confirmDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordResetTokenDTO that = (PasswordResetTokenDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(passwordResetToken, that.passwordResetToken) && Objects.equals(userId, that.userId) && Objects.equals(userName, that.userName) && Objects.equals(email, that.email) && Objects.equals(createDate, that.createDate) && Objects.equals(expireDate, that.expireDate) && Objects.equals(confirmDate, that.confirmDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passwordResetToken, userId, userName, email, createDate, expireDate, confirmDate);
    }

    @Override
    public String toString() {
        return "ConfirmationTokenDTO{" +
                "id=" + id +
                ", passwordResetToken='" + passwordResetToken + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", createDate=" + createDate +
                ", expireDate=" + expireDate +
                ", confirmDate=" + confirmDate +
                '}';
    }


    public static final class Builder {
        private Long id;
        private String passwordResetToken;
        private Long userId;
        private String userName;
        private String email;
        private LocalDateTime createDate;
        private LocalDateTime expireDate;
        private LocalDateTime confirmDate;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder passwordResetToken(String passwordResetToken) {
            this.passwordResetToken = passwordResetToken;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder createDate(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder expireDate(LocalDateTime expireDate) {
            this.expireDate = expireDate;
            return this;
        }

        public Builder confirmDate(LocalDateTime confirmDate) {
            this.confirmDate = confirmDate;
            return this;
        }

        public PasswordResetTokenDTO build() {
            return new PasswordResetTokenDTO(this);
        }
    }
}
