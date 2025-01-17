package pl.com.chrzanowski.sma.email.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.chrzanowski.sma.common.enumeration.Language;
import pl.com.chrzanowski.sma.common.enumeration.MailEvent;
import pl.com.chrzanowski.sma.user.model.User;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "send_emails")
public class SendEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private User user;

    @Column(name = "create_date")
    private Instant createdDatetime;

}
