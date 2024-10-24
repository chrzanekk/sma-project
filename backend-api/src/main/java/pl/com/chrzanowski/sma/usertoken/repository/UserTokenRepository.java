package pl.com.chrzanowski.sma.usertoken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.usertoken.model.UserToken;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findUserTokensByToken(String token);

    void deleteById(Long id);
}
