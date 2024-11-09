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
        User user = User.builder().email("test@example.com").username("testuser").password("password123").build();
        userRepository.save(user);

        // When
        var foundUser = userRepository.findByEmail("test@example.com");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void findByEmail_Negative() {
        // When
        var foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertTrue(foundUser.isEmpty());
    }


    @Test
    void findByUsername_Positive() {
        // Given
        User user = User.builder().email("test@example.com").username("testuser").password("password123").build();
        userRepository.save(user);

        // When
        var foundUser = userRepository.findByUsername("testuser");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void findByUsername_Negative() {
        // When
        var foundUser = userRepository.findByUsername("nonexistentuser");

        // Then
        assertTrue(foundUser.isEmpty());
    }


    @Test
    void existsByEmail_Positive() {
        // Given
        User user = User.builder().email("test@example.com").username("testuser").password("password123").build();
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
    void existsByUsername_Positive() {
        // Given
        User user = User.builder().email("test@example.com").username("testuser").password("password123").build();
        userRepository.save(user);

        // When
        boolean exists = userRepository.existsByUsername("testuser");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByUsername_Negative() {
        // When
        boolean exists = userRepository.existsByUsername("nonexistentuser");

        // Then
        assertFalse(exists);
    }

}