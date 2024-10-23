package pl.com.chrzanowski.sma.email;

import lombok.*;
import pl.com.chrzanowski.sma.enumeration.Language;
import pl.com.chrzanowski.sma.enumeration.MailEvent;

import java.time.LocalDateTime;


@Builder
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
    LocalDateTime createDatetime;
}
