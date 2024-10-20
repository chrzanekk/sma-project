package pl.com.chrzanowski.scma.service.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class ConfirmationTokenDTO {

    private Long id;
    private String confirmationToken;
    private Long userId;
    private String userName;
    private String email;
    private LocalDateTime createDate;
    private LocalDateTime expireDate;
    private LocalDateTime confirmDate;

    public ConfirmationTokenDTO() {
    }

    public ConfirmationTokenDTO(Long id,
                                String confirmationToken,
                                Long userId,
                                String userName,
                                String email,
                                LocalDateTime createDate,
                                LocalDateTime expireDate,
                                LocalDateTime confirmDate) {
        this.id = id;
        this.confirmationToken = confirmationToken;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.createDate = createDate;
        this.expireDate = expireDate;
        this.confirmDate = confirmDate;
    }

    private ConfirmationTokenDTO(Builder builder) {
        id = builder.id;
        confirmationToken = builder.confirmationToken;
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

    public static Builder builder(ConfirmationTokenDTO copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.confirmationToken = copy.getConfirmationToken();
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

    public String getConfirmationToken() {
        return confirmationToken;
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
        ConfirmationTokenDTO that = (ConfirmationTokenDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(confirmationToken, that.confirmationToken) && Objects.equals(userId, that.userId) && Objects.equals(userName, that.userName) && Objects.equals(email, that.email) && Objects.equals(createDate, that.createDate) && Objects.equals(expireDate, that.expireDate) && Objects.equals(confirmDate, that.confirmDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, confirmationToken, userId, userName, email, createDate, expireDate, confirmDate);
    }

    @Override
    public String toString() {
        return "ConfirmationTokenDTO{" +
                "id=" + id +
                ", confirmationToken='" + confirmationToken + '\'' +
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
        private String confirmationToken;
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

        public Builder confirmationToken(String confirmationToken) {
            this.confirmationToken = confirmationToken;
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

        public ConfirmationTokenDTO build() {
            return new ConfirmationTokenDTO(this);
        }
    }
}
