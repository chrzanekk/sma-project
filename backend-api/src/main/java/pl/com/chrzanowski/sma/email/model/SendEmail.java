package pl.com.chrzanowski.sma.email.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.com.chrzanowski.sma.common.enumeration.Language;
import pl.com.chrzanowski.sma.common.enumeration.MailEvent;
import pl.com.chrzanowski.sma.user.model.User;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "send_emails")
public class SendEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "send_emails_seq")
    @SequenceGenerator(name = "send_emails_seq", sequenceName = "send_emails_sequence", allocationSize = 1)
    @Column(nullable = false)
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

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false, name = "user_id")
    @ToString.Exclude
    private User user;

    @Column(name = "create_date")
    private Instant createdDatetime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendEmail sendEmail = (SendEmail) o;
        return id != null && id.equals(sendEmail.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "SendEmail{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", mailEvent=" + mailEvent +
                ", language=" + language +
                ", createdDatetime=" + createdDatetime +
                '}';
    }
}
