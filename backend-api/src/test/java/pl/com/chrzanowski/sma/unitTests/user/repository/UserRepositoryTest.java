package pl.com.chrzanowski.sma.unitTests.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest extends AbstractTestContainers {

    @Autowired
    UserRepository userRepository;

    @Test
    void findByEmail_Positive() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .login("testuser")
                .password("password123")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        userRepository.save(user);

        // When
        var foundUser = userRepository.findByEmail("test@example.com");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getLogin());
    }

    @Test
    void findByEmail_Negative() {
        // When
        var foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertTrue(foundUser.isEmpty());
    }


    @Test
    void findByLogin_Positive() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .login("testuser")
                .password("password123")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        userRepository.save(user);

        // When
        var foundUser = userRepository.findByLogin("testuser");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void findByLogin_Negative() {
        // When
        var foundUser = userRepository.findByLogin("nonexistentuser");

        // Then
        assertTrue(foundUser.isEmpty());
    }


    @Test
    void existsByEmail_Positive() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .login("testuser")
                .password("password123")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        userRepository.save(user);

        // When
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByEmail_Negative() {
        // When
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertFalse(exists);
    }


    @Test
    void existsByLogin_Positive() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .login("testuser")
                .password("password123")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        userRepository.save(user);

        // When
        boolean exists = userRepository.existsByLogin("testuser");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByLogin_Negative() {
        // When
        boolean exists = userRepository.existsByLogin("nonexistentuser");

        // Then
        assertFalse(exists);
    }

}