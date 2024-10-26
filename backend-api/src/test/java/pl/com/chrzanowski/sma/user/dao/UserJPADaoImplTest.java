package pl.com.chrzanowski.sma.user.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserJPADaoImplTest {

    @InjectMocks
    private UserJPADaoImpl userJPADaoImpl;

    @Mock
    private UserRepository userRepository;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void save_Positive() {
        // Given
        User user = User.builder().username("testuser").email("test@example.com").build();
        when(userRepository.save(user)).thenReturn(user);

        // When
        User savedUser = userJPADaoImpl.save(user);

        // Then
        assertEquals(user, savedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void save_Negative() {
        // Given
        User user = User.builder().username("testuser").email("test@example.com").build();
        when(userRepository.save(user)).thenThrow(new RuntimeException("Save failed"));

        // When / Then
        assertThrows(RuntimeException.class, () -> userJPADaoImpl.save(user));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findByEmail_Positive() {
        // Given
        String email = "test@example.com";
        User user = User.builder().email(email).build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        Optional<User> foundUser = userJPADaoImpl.findByEmail(email);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByEmail_Negative() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Optional<User> foundUser = userJPADaoImpl.findByEmail(email);

        // Then
        assertTrue(foundUser.isEmpty());
        verify(userRepository, times(1)).findByEmail(email);
    }


    @Test
    void findByUsername_Positive() {
        // Given
        String username = "testuser";
        User user = User.builder().username(username).build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        Optional<User> foundUser = userJPADaoImpl.findByUsername(username);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void findByUsername_Negative() {
        // Given
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When
        Optional<User> foundUser = userJPADaoImpl.findByUsername(username);

        // Then
        assertTrue(foundUser.isEmpty());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void existsByEmail_Positive() {
        // Given
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // When
        Boolean exists = userJPADaoImpl.existsByEmail(email);

        // Then
        assertTrue(exists);
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void existsByEmail_Negative() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // When
        Boolean exists = userJPADaoImpl.existsByEmail(email);

        // Then
        assertFalse(exists);
        verify(userRepository, times(1)).existsByEmail(email);
    }


    @Test
    void existsByUsername_Positive() {
        // Given
        String username = "testuser";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // When
        Boolean exists = userJPADaoImpl.existsByUsername(username);

        // Then
        assertTrue(exists);
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void existsByUsername_Negative() {
        // Given
        String username = "nonexistentuser";
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // When
        Boolean exists = userJPADaoImpl.existsByUsername(username);

        // Then
        assertFalse(exists);
        verify(userRepository, times(1)).existsByUsername(username);
    }

    @Test
    void findById_Positive() {
        // Given
        Long id = 1L;
        User user = User.builder().id(id).build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // When
        Optional<User> foundUser = userJPADaoImpl.findById(id);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void findById_Negative() {
        // Given
        Long id = 999L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<User> foundUser = userJPADaoImpl.findById(id);

        // Then
        assertTrue(foundUser.isEmpty());
        verify(userRepository, times(1)).findById(id);
    }


    @Test
    void findAll_Positive() {
        // Given
        User user1 = User.builder().username("user1").build();
        User user2 = User.builder().username("user2").build();
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // When
        List<User> users = userJPADaoImpl.findAll();

        // Then
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findAll_Negative() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<User> users = userJPADaoImpl.findAll();

        // Then
        assertTrue(users.isEmpty());
        verify(userRepository, times(1)).findAll();
    }


    @Test
    void findAll_WithSpecificationAndPageable_Positive() {
        // Given
        Specification<User> specification = mock(Specification.class);
        Pageable pageable = PageRequest.of(0, 2);
        User user1 = User.builder().username("user1").build();
        Page<User> page = new PageImpl<>(List.of(user1), pageable, 1);
        when(userRepository.findAll(specification, pageable)).thenReturn(page);

        // When
        Page<User> result = userJPADaoImpl.findAll(specification, pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().contains(user1));
        verify(userRepository, times(1)).findAll(specification, pageable);
    }

    @Test
    void findAll_WithSpecificationAndPageable_Negative() {
        // Given
        Specification<User> specification = mock(Specification.class);
        Pageable pageable = PageRequest.of(0, 2);
        Page<User> page = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(userRepository.findAll(specification, pageable)).thenReturn(page);

        // When
        Page<User> result = userJPADaoImpl.findAll(specification, pageable);

        // Then
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll(specification, pageable);
    }

    @Test
    void findAll_WithSpecification_Positive() {
        // Given
        Specification<User> specification = mock(Specification.class);
        User user1 = User.builder().username("user1").build();
        when(userRepository.findAll(specification)).thenReturn(List.of(user1));

        // When
        List<User> users = userJPADaoImpl.findAll(specification);

        // Then
        assertEquals(1, users.size());
        assertTrue(users.contains(user1));
        verify(userRepository, times(1)).findAll(specification);
    }

    @Test
    void findAll_WithSpecification_Negative() {
        // Given
        Specification<User> specification = mock(Specification.class);
        when(userRepository.findAll(specification)).thenReturn(Collections.emptyList());

        // When
        List<User> users = userJPADaoImpl.findAll(specification);

        // Then
        assertTrue(users.isEmpty());
        verify(userRepository, times(1)).findAll(specification);
    }


    @Test
    void deleteById_Positive() {
        // Given
        Long id = 1L;

        // When
        userJPADaoImpl.deleteById(id);

        // Then
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_Negative() {
        // Given
        Long id = 999L;
        doThrow(new RuntimeException("Delete failed")).when(userRepository).deleteById(id);

        // When / Then
        assertThrows(RuntimeException.class, () -> userJPADaoImpl.deleteById(id));
        verify(userRepository, times(1)).deleteById(id);
    }

}