package pl.com.chrzanowski.sma.email;

import lombok.*;
import pl.com.chrzanowski.sma.enumeration.Language;
import pl.com.chrzanowski.sma.enumeration.MailEvent;

import java.time.Instant;
import java.time.LocalDateTime;


@Builder(toBuilder = true)
@Value
@AllArgsConstructor
public class SentEmailDTO {

    Long id;
    Long userId;
    String userEmail;
    String title;
    String content;
    MailEvent mailEvent;
    Language language;
    Instant createDatetime;
}
