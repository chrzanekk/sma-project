package pl.com.chrzanowski.sma.usertoken.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.usertoken.model.UserToken;
import pl.com.chrzanowski.sma.usertoken.repository.UserTokenRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTokenJPADaoImplTest {

    @InjectMocks
    private UserTokenJPADaoImpl userTokenJPADaoImpl;
    private AutoCloseable closeable;

    @Mock
    private UserTokenRepository userTokenRepository;
    @BeforeEach
    void setUp() throws Exception {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void saveToken_Positive() {
        // Given
        UserToken userToken = UserToken.builder().token("testToken").build();
        when(userTokenRepository.save(userToken)).thenReturn(userToken);

        // When
        UserToken savedToken = userTokenJPADaoImpl.saveToken(userToken);

        // Then
        assertEquals(userToken, savedToken);
        verify(userTokenRepository, times(1)).save(userToken);
    }

    @Test
    void saveToken_Negative() {
        // Given
        UserToken userToken = UserToken.builder().token("testToken").build();
        when(userTokenRepository.save(userToken)).thenThrow(new RuntimeException("Save failed"));

        // When / Then
        assertThrows(RuntimeException.class, () -> userTokenJPADaoImpl.saveToken(userToken));
        verify(userTokenRepository, times(1)).save(userToken);
    }


    @Test
    void updateToken_Positive() {
        // Given
        UserToken userToken = UserToken.builder().token("updatedToken").build();
        when(userTokenRepository.save(userToken)).thenReturn(userToken);

        // When
        UserToken updatedToken = userTokenJPADaoImpl.updateToken(userToken);

        // Then
        assertEquals(userToken, updatedToken);
        verify(userTokenRepository, times(1)).save(userToken);
    }

    @Test
    void updateToken_Negative() {
        // Given
        UserToken userToken = UserToken.builder().token("updatedToken").build();
        when(userTokenRepository.save(userToken)).thenThrow(new RuntimeException("Update failed"));

        // When / Then
        assertThrows(RuntimeException.class, () -> userTokenJPADaoImpl.updateToken(userToken));
        verify(userTokenRepository, times(1)).save(userToken);
    }

    @Test
    void findUserTokensByToken_Positive() {
        // Given
        String token = "testToken";
        UserToken userToken = UserToken.builder().token(token).build();
        when(userTokenRepository.findUserTokensByToken(token)).thenReturn(Optional.of(userToken));

        // When
        Optional<UserToken> foundToken = userTokenJPADaoImpl.findUserTokensByToken(token);

        // Then
        assertTrue(foundToken.isPresent());
        assertEquals(userToken, foundToken.get());
        verify(userTokenRepository, times(1)).findUserTokensByToken(token);
    }

    @Test
    void findUserTokensByToken_Negative() {
        // Given
        String token = "nonExistentToken";
        when(userTokenRepository.findUserTokensByToken(token)).thenReturn(Optional.empty());

        // When
        Optional<UserToken> foundToken = userTokenJPADaoImpl.findUserTokensByToken(token);

        // Then
        assertTrue(foundToken.isEmpty());
        verify(userTokenRepository, times(1)).findUserTokensByToken(token);
    }

    @Test
    void deleteTokenById_Positive() {
        // Given
        Long tokenId = 1L;

        // When
        userTokenJPADaoImpl.deleteTokenById(tokenId);

        // Then
        verify(userTokenRepository, times(1)).deleteById(tokenId);
    }

    @Test
    void deleteTokenById_Negative() {
        // Given
        Long nonExistentId = 999L;
        doThrow(new RuntimeException("Delete failed")).when(userTokenRepository).deleteById(nonExistentId);

        // When / Then
        assertThrows(RuntimeException.class, () -> userTokenJPADaoImpl.deleteTokenById(nonExistentId));
        verify(userTokenRepository, times(1)).deleteById(nonExistentId);
    }

}