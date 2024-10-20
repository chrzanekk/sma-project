package pl.com.chrzanowski.scma.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import pl.com.chrzanowski.scma.domain.enumeration.Language;
import pl.com.chrzanowski.scma.domain.enumeration.MailEvent;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sent_emails")
public class SentEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    @Size(max = 5000)
    private String content;

    @Column(name = "event")
    @Enumerated(EnumType.STRING)
    private MailEvent mailEvent;
    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(name = "create_time")
    private LocalDateTime createDatetime;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public SentEmail setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SentEmail setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public SentEmail setContent(String content) {
        this.content = content;
        return this;
    }

    public MailEvent getMailEvent() {
        return mailEvent;
    }

    public SentEmail setMailEvent(MailEvent mailEvent) {
        this.mailEvent = mailEvent;
        return this;
    }

    public Language getLanguage() {
        return language;
    }

    public SentEmail setLanguage(Language language) {
        this.language = language;
        return this;
    }

    public User getUser() {
        return user;
    }

    public SentEmail setUser(User user) {
        this.user = user;
        return this;
    }

    public LocalDateTime getCreateDatetime() {
        return createDatetime;
    }

    public SentEmail setCreateDatetime(LocalDateTime createDatetime) {
        this.createDatetime = createDatetime;
        return this;
    }

    public SentEmail() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SentEmail sentEmail = (SentEmail) o;
        return Objects.equals(id, sentEmail.id) && Objects.equals(title, sentEmail.title) && Objects.equals(content, sentEmail.content) && mailEvent == sentEmail.mailEvent && language == sentEmail.language && Objects.equals(createDatetime, sentEmail.createDatetime) && Objects.equals(user, sentEmail.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, mailEvent, language, createDatetime, user);
    }

    @Override
    public String toString() {
        return "SentEmail{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", mailEvent=" + mailEvent +
                ", language=" + language +
                ", createDatetime=" + createDatetime +
                ", user=" + user +
                '}';
    }


}
