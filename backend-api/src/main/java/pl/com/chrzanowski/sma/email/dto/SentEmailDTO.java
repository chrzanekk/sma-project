package pl.com.chrzanowski.sma.email.dto;

import lombok.*;
import pl.com.chrzanowski.sma.common.enumeration.Language;
import pl.com.chrzanowski.sma.common.enumeration.MailEvent;

import java.time.Instant;


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
