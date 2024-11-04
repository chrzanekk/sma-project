package pl.com.chrzanowski.sma.usertoken.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.chrzanowski.sma.common.enumeration.TokenType;
import pl.com.chrzanowski.sma.usertoken.model.UserToken;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findUserTokenByToken(String token);

    void deleteById(Long id);

    Optional<UserToken> findByUserEmailAndTokenType(@NotBlank @Size(max = 50) String user_email, TokenType tokenType);
}
