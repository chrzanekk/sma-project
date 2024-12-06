package pl.com.chrzanowski.sma.unitTests.user.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.user.dao.UserJPADaoImpl;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.repository.UserRepository;
import pl.com.chrzanowski.sma.user.service.filter.UserQuerySpec;

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
        User user = User.builder().login("testuser").email("test@example.com").build();
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
        User user = User.builder().login("testuser").email("test@example.com").build();
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
    void findByLogin_Positive() {
        // Given
        String login = "testuser";
        User user = User.builder().login(login).build();
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        // When
        Optional<User> foundUser = userJPADaoImpl.findByLogin(login);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(userRepository, times(1)).findByLogin(login);
    }

    @Test
    void findByLogin_Negative() {
        // Given
        String login = "nonexistentuser";
        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        // When
        Optional<User> foundUser = userJPADaoImpl.findByLogin(login);

        // Then
        assertTrue(foundUser.isEmpty());
        verify(userRepository, times(1)).findByLogin(login);
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
    void existsByLogin_Positive() {
        // Given
        String login = "testuser";
        when(userRepository.existsByLogin(login)).thenReturn(true);

        // When
        Boolean exists = userJPADaoImpl.existsByLogin(login);

        // Then
        assertTrue(exists);
        verify(userRepository, times(1)).existsByLogin(login);
    }

    @Test
    void existsByLogin_Negative() {
        // Given
        String login = "nonexistentuser";
        when(userRepository.existsByLogin(login)).thenReturn(false);

        // When
        Boolean exists = userJPADaoImpl.existsByLogin(login);

        // Then
        assertFalse(exists);
        verify(userRepository, times(1)).existsByLogin(login);
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
        User user1 = User.builder().login("user1").build();
        User user2 = User.builder().login("user2").build();
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
        BooleanBuilder specification = mock(BooleanBuilder.class);
        UserQuerySpec userQuerySpec = mock(UserQuerySpec.class);
        JPQLQuery<User> query = mock(JPQLQuery.class);
        UserJPADaoImpl userJPADaoImpl = new UserJPADaoImpl(null, userQuerySpec);

        User user1 = User.builder().login("user1").build();
        List<User> userList = List.of(user1);

        when(query.offset(0L)).thenReturn(query);
        when(query.limit(2)).thenReturn(query);
        when(query.fetchCount()).thenReturn(1L);
        when(query.fetch()).thenReturn(userList);

        // Mock buildQuery
        when(userQuerySpec.buildQuery(specification)).thenReturn(query);

        // When
        var pageable = org.springframework.data.domain.PageRequest.of(0, 2);
        var result = userJPADaoImpl.findAll(specification, pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().contains(user1));
    }

    @Test
    void findAll_WithSpecificationAndPageable_Negative() {
        // Given
        BooleanBuilder specification = mock(BooleanBuilder.class);
        UserQuerySpec userQuerySpec = mock(UserQuerySpec.class);
        JPQLQuery<User> query = mock(JPQLQuery.class);
        UserJPADaoImpl userJPADaoImpl = new UserJPADaoImpl(null, userQuerySpec);

        when(query.offset(0L)).thenReturn(query);
        when(query.limit(2)).thenReturn(query);
        when(query.fetchCount()).thenReturn(0L);
        when(query.fetch()).thenReturn(Collections.emptyList());

        // Mock buildQuery
        when(userQuerySpec.buildQuery(specification)).thenReturn(query);

        // When
        var pageable = org.springframework.data.domain.PageRequest.of(0, 2);
        var result = userJPADaoImpl.findAll(specification, pageable);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_WithSpecification_Positive() {
        // Given
        BooleanBuilder specification = mock(BooleanBuilder.class);
        UserQuerySpec userQuerySpec = mock(UserQuerySpec.class);

        UserJPADaoImpl userJPADaoImpl = new UserJPADaoImpl(null, userQuerySpec);

        @SuppressWarnings("unchecked")
        JPQLQuery<User> query = mock(JPQLQuery.class);

        User user1 = User.builder().login("user1").build();
        List<User> userList = List.of(user1);

        when(query.fetch()).thenReturn(userList);

        // Mock buildQuery
        when(userQuerySpec.buildQuery(specification)).thenReturn(query);

        // When
        List<User> result = userJPADaoImpl.findAll(specification);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.contains(user1));
        verify(query, times(1)).fetch();

    }

    @Test
    void findAll_WithSpecification_Negative() {
        // Given
        BooleanBuilder specification = mock(BooleanBuilder.class);
        UserQuerySpec userQuerySpec = mock(UserQuerySpec.class);
        JPQLQuery<User> query = mock(JPQLQuery.class);
        UserJPADaoImpl userJPADaoImpl = new UserJPADaoImpl(null, userQuerySpec);

        when(query.fetch()).thenReturn(Collections.emptyList());

        // Mock buildQuery
        when(userQuerySpec.buildQuery(specification)).thenReturn(query);

        // When
        List<User> result = userJPADaoImpl.findAll(specification);

        // Then
        assertTrue(result.isEmpty());
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