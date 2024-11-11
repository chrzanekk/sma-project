package pl.com.chrzanowski.sma.usertoken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.chrzanowski.sma.usertoken.model.UserToken;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findUserTokenByToken(String token);

    void deleteById(Long id);

    void deleteUserTokenByUserId(Long userId);
}
