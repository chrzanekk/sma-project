package pl.com.chrzanowski.scma.auth.resettoken;

import jakarta.persistence.*;
import pl.com.chrzanowski.scma.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "creation_date")
    private LocalDateTime createDate;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;
    @Column(name = "reset_date")
    private LocalDateTime confirmDate;

    public PasswordResetToken(Long id,
                              String passwordResetToken,
                              User user,
                              LocalDateTime createDate,
                              LocalDateTime expireDate,
                              LocalDateTime confirmDate) {
        this.id = id;
        this.passwordResetToken = passwordResetToken;
        this.user = user;
        this.createDate = createDate;
        this.expireDate = expireDate;
        this.confirmDate = confirmDate;
    }

    public PasswordResetToken() {
    }

    public Long getId() {
        return id;
    }

    public PasswordResetToken setId(Long id) {
        this.id = id;
        return this;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public PasswordResetToken setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
        return this;
    }

    public User getUser() {
        return user;
    }

    public PasswordResetToken setUser(User user) {
        this.user = user;
        return this;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public PasswordResetToken setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public PasswordResetToken setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
        return this;
    }

    public LocalDateTime getConfirmDate() {
        return confirmDate;
    }

    public PasswordResetToken setConfirmDate(LocalDateTime confirmDate) {
        this.confirmDate = confirmDate;
        return this;
    }
}
