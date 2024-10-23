package pl.com.chrzanowski.sma.auth.usertokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findUserTokensByToken(String token);

    void deleteById(Long id);
}
