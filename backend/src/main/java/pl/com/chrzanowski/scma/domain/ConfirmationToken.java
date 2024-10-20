package pl.com.chrzanowski.scma.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cofirmation_tokens")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "creation_date")
    private LocalDateTime createDate;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;
    @Column(name = "confirm_date")
    private LocalDateTime confirmDate;

    public ConfirmationToken(Long id,
                             String confirmationToken,
                             User user,
                             LocalDateTime createDate,
                             LocalDateTime expireDate,
                             LocalDateTime confirmDate) {
        this.id = id;
        this.confirmationToken = confirmationToken;
        this.user = user;
        this.createDate = createDate;
        this.expireDate = expireDate;
        this.confirmDate = confirmDate;
    }

    public ConfirmationToken() {
    }

    public Long getId() {
        return id;
    }

    public ConfirmationToken setId(Long id) {
        this.id = id;
        return this;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public ConfirmationToken setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
        return this;
    }

    public User getUser() {
        return user;
    }

    public ConfirmationToken setUser(User user) {
        this.user = user;
        return this;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public ConfirmationToken setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public ConfirmationToken setExpireDate(LocalDateTime validDate) {
        this.expireDate = validDate;
        return this;
    }

    public LocalDateTime getConfirmDate() {
        return confirmDate;
    }

    public ConfirmationToken setConfirmDate(LocalDateTime confirmDate) {
        this.confirmDate = confirmDate;
        return this;
    }
}
