package pl.com.chrzanowski.sma.unitTests.usertoken.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.common.enumeration.TokenType;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.repository.UserRepository;
import pl.com.chrzanowski.sma.usertoken.model.UserToken;
import pl.com.chrzanowski.sma.usertoken.repository.UserTokenRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserTokenRepositoryTest extends AbstractTestContainers {

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findUserTokenByToken_Positive() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .login("testuser")
                .password("password123")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        userRepository.save(user);
        UserToken token = UserToken.builder()
                .token("testToken123")
                .user(user)
                .createDate(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusDays(1))
                .tokenType(TokenType.PASSWORD_RESET_TOKEN)
                .build();
        userTokenRepository.save(token);

        // When
        var foundToken = userTokenRepository.findUserTokenByToken("testToken123");

        // Then
        assertTrue(foundToken.isPresent());
        assertEquals("testToken123", foundToken.get().getToken());
        assertEquals("testuser", foundToken.get().getUser().getLogin());
    }

    @Test
    void findUserTokenByToken_Negative() {
        // When
        var foundToken = userTokenRepository.findUserTokenByToken("nonexistentToken");

        // Then
        assertTrue(foundToken.isEmpty());
    }

    @Test
    void deleteById_Positive() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .login("testuser")
                .password("password123")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        userRepository.save(user);
        UserToken token = UserToken.builder()
                .token("testToken123")
                .user(user)
                .createDate(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusDays(1))
                .tokenType(TokenType.CONFIRMATION_TOKEN)
                .build();
        UserToken savedToken = userTokenRepository.save(token);

        // When
        userTokenRepository.deleteById(savedToken.getId());
        var deletedToken = userTokenRepository.findById(savedToken.getId());

        // Then
        assertTrue(deletedToken.isEmpty());
    }

    @Test
    void deleteById_Negative() {
        // Given
        Long nonexistentId = 999L;

        // When
        userTokenRepository.deleteById(nonexistentId);
        var deletedToken = userTokenRepository.findById(nonexistentId);

        // Then
        assertTrue(deletedToken.isEmpty());
    }

    @Test
    void deleteByUserId_Positive() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .login("testuser")
                .password("password123")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        userRepository.save(user);
        UserToken token = UserToken.builder()
                .token("testToken123")
                .user(user)
                .createDate(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusDays(1))
                .tokenType(TokenType.CONFIRMATION_TOKEN)
                .build();
        UserToken savedToken = userTokenRepository.save(token);

        // When
        userTokenRepository.deleteUserTokenByUserId(user.getId());
        var deletedToken = userTokenRepository.findById(savedToken.getId());

        // Then
        assertTrue(deletedToken.isEmpty());
    }

    @Test
    void deleteByUserId_Negative() {
        // Given
        Long nonexistentId = 999L;

        // When
        userTokenRepository.deleteUserTokenByUserId(nonexistentId);
        var deletedToken = userTokenRepository.findById(nonexistentId);

        // Then
        assertTrue(deletedToken.isEmpty());
    }
}