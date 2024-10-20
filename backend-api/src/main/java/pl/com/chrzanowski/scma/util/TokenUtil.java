package pl.com.chrzanowski.scma.util;

import org.springframework.stereotype.Component;
import pl.com.chrzanowski.scma.exception.ExpiredTokenException;

import java.time.LocalDateTime;

@Component
public class TokenUtil {

    public static void validateTokenTime(LocalDateTime createDate, Long tokenValidityInMinutes) {
        if (LocalDateTime.now().isAfter(createDate.plusMinutes(tokenValidityInMinutes))) {
            throw new ExpiredTokenException("Token expired");
        }
    }
}
