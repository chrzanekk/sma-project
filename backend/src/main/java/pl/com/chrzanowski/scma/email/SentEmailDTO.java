package pl.com.chrzanowski.scma.email;

import pl.com.chrzanowski.scma.enumeration.Language;
import pl.com.chrzanowski.scma.enumeration.MailEvent;

import java.time.LocalDateTime;
import java.util.Objects;

public class SentEmailDTO {
    private final Long id;
    private final Long userId;
    private final String userEmail;
    private final String title;
    private final String content;
    private final MailEvent mailEvent;
    private final Language language;
    private final LocalDateTime createDatetime;

    public SentEmailDTO(Long id,
                        Long userId,
                        String userEmail, String title,
                        String content,
                        MailEvent mailEvent,
                        Language language, LocalDateTime createDatetime) {
        this.id = id;
        this.userId = userId;
        this.userEmail = userEmail;
        this.title = title;
        this.content = content;
        this.mailEvent = mailEvent;
        this.language = language;
        this.createDatetime = createDatetime;
    }

    private SentEmailDTO(Builder builder) {
        id = builder.id;
        userId = builder.userId;
        userEmail = builder.userEmail;
        title = builder.title;
        content = builder.content;
        mailEvent = builder.mailEvent;
        language = builder.language;
        createDatetime = builder.createDatetime;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(SentEmailDTO copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.userId = copy.getUserId();
        builder.userEmail = copy.getUserEmail();
        builder.title = copy.getTitle();
        builder.content = copy.getContent();
        builder.mailEvent = copy.getMailEvent();
        builder.language = copy.getLanguage();
        builder.createDatetime = copy.getCreateDatetime();
        return builder;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public MailEvent getMailEvent() {
        return mailEvent;
    }

    public Language getLanguage() {
        return language;
    }

    public LocalDateTime getCreateDatetime() {
        return createDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SentEmailDTO that = (SentEmailDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(userEmail, that.userEmail) && Objects.equals(title, that.title) && Objects.equals(content, that.content) && mailEvent == that.mailEvent && language == that.language && Objects.equals(createDatetime, that.createDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, userEmail, title, content, mailEvent, language, createDatetime);
    }

    @Override
    public String toString() {
        return "SentEmailDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", userEmail='" + userEmail + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", mailEvent=" + mailEvent +
                ", language=" + language +
                ", createDatetime=" + createDatetime +
                '}';
    }


    public static final class Builder {
        private Long id;
        private Long userId;
        private String userEmail;
        private String title;
        private String content;
        private MailEvent mailEvent;
        private Language language;
        private LocalDateTime createDatetime;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder userEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder mailEvent(MailEvent mailEvent) {
            this.mailEvent = mailEvent;
            return this;
        }

        public Builder language(Language language) {
            this.language = language;
            return this;
        }

        public Builder createDatetime(LocalDateTime createDatetime) {
            this.createDatetime = createDatetime;
            return this;
        }

        public SentEmailDTO build() {
            return new SentEmailDTO(this);
        }
    }
}
