package pl.com.chrzanowski.sma.usertoken.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import pl.com.chrzanowski.sma.common.enumeration.TokenType;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@AllArgsConstructor
@Value
public class UserTokenDTO {

    Long id;
    String token;
    Long userId;
    String login;
    String email;
    LocalDateTime createDate;
    LocalDateTime expireDate;
    LocalDateTime useDate;
    TokenType tokenType;
}
