package pl.com.chrzanowski.sma.unitTests.usertoken.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.enumeration.TokenType;
import pl.com.chrzanowski.sma.common.exception.ObjectNotFoundException;
import pl.com.chrzanowski.sma.common.exception.TokenException;
import pl.com.chrzanowski.sma.common.exception.UserNotFoundException;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.usertoken.dao.UserTokenDao;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;
import pl.com.chrzanowski.sma.usertoken.mapper.UserTokenMapper;
import pl.com.chrzanowski.sma.usertoken.model.UserToken;
import pl.com.chrzanowski.sma.usertoken.service.UserTokenServiceImpl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserTokenServiceImplTest {

    @Mock
    private UserTokenDao userTokenDao;

    @Mock
    private UserTokenMapper userTokenMapper;

    @InjectMocks
    private UserTokenServiceImpl userTokenService;

    private UserTokenDTO userTokenDTO;
    private UserToken userToken;
    private UserDTO userDTO;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userDTO = UserDTO.builder()
                .id(1L)
                .email("test@example.com")
                .login("testUser")
                .build();

        userTokenDTO = UserTokenDTO.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .userId(1L)
                .login("testUser")
                .email("test@example.com")
                .createDate(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusMinutes(30))
                .tokenType(TokenType.PASSWORD_RESET_TOKEN)
                .build();

        userToken = UserToken.builder()
                .id(1L)
                .token(userTokenDTO.getToken())
                .createDate(userTokenDTO.getCreateDate())
                .expireDate(userTokenDTO.getExpireDate())
                .tokenType(TokenType.PASSWORD_RESET_TOKEN)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGenerateTokenSuccess() {
        String token = userTokenService.generateToken();

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testSaveTokenSuccess() {
        when(userTokenMapper.toEntity(any(UserTokenDTO.class))).thenReturn(userToken);
        when(userTokenDao.saveToken(any(UserToken.class))).thenReturn(userToken);
        when(userTokenMapper.toDto(any(UserToken.class))).thenReturn(userTokenDTO);

        UserTokenDTO result = userTokenService.saveToken(userTokenDTO.getToken(), userDTO, TokenType.PASSWORD_RESET_TOKEN);

        assertNotNull(result);
        assertEquals(userTokenDTO.getToken(), result.getToken());

        verify(userTokenMapper, times(1)).toEntity(any(UserTokenDTO.class));
        verify(userTokenDao, times(1)).saveToken(any(UserToken.class));
        verify(userTokenMapper, times(1)).toDto(any(UserToken.class));
    }

    @Test
    void testSaveTokenUserNull() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userTokenService.saveToken(userTokenDTO.getToken(), null, TokenType.PASSWORD_RESET_TOKEN);
        });

        assertEquals("User must not be null", exception.getMessage());

        verify(userTokenDao, times(0)).saveToken(any(UserToken.class));
    }

    @Test
    void testSaveTokenWithEmptyToken() {
        TokenException exception = assertThrows(TokenException.class, () -> {
            userTokenService.saveToken(null, userDTO, TokenType.PASSWORD_RESET_TOKEN);
        });

        assertEquals("Token must not be null or empty", exception.getMessage());

        verify(userTokenDao, never()).saveToken(any(UserToken.class));
    }


    @Test
    void testUpdateTokenSuccess() {

        UserToken updatedUserToken = UserToken.builder()
                .id(1L)
                .token(userTokenDTO.getToken())
                .createDate(userTokenDTO.getCreateDate())
                .expireDate(userTokenDTO.getExpireDate())
                .useDate(userTokenDTO.getCreateDate().plusMinutes(30))
                .tokenType(TokenType.PASSWORD_RESET_TOKEN)
                .build();

        UserTokenDTO updatedUserTokenDTO = UserTokenDTO.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .userId(1L)
                .login("testUser")
                .email("test@example.com")
                .createDate(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusMinutes(30))
                .useDate(updatedUserToken.getUseDate())
                .tokenType(TokenType.PASSWORD_RESET_TOKEN)
                .build();
        when(userTokenDao.updateToken(any(UserToken.class))).thenReturn(updatedUserToken);
        when(userTokenMapper.toEntity(any(UserTokenDTO.class))).thenReturn(updatedUserToken);
        when(userTokenMapper.toDto(any(UserToken.class))).thenReturn(updatedUserTokenDTO);

        UserTokenDTO result = userTokenService.updateToken(userTokenDTO);

        assertNotNull(result);
        assertEquals(updatedUserTokenDTO.getToken(), result.getToken());

        verify(userTokenMapper, times(1)).toEntity(any(UserTokenDTO.class));
        verify(userTokenDao, times(1)).updateToken(any(UserToken.class));
        verify(userTokenMapper, times(1)).toDto(any(UserToken.class));
    }

    @Test
    void testGetTokenDataSuccess() {
        when(userTokenDao.findUserTokenByToken(anyString())).thenReturn(Optional.of(userToken));
        when(userTokenMapper.toDto(any(UserToken.class))).thenReturn(userTokenDTO);

        UserTokenDTO result = userTokenService.getTokenData(userTokenDTO.getToken());

        assertNotNull(result);
        assertEquals(userTokenDTO.getToken(), result.getToken());

        verify(userTokenDao, times(1)).findUserTokenByToken(anyString());
        verify(userTokenMapper, times(1)).toDto(any(UserToken.class));
    }

    @Test
    void testGetTokenDataNotFound() {
        when(userTokenDao.findUserTokenByToken(anyString())).thenReturn(Optional.empty());

        TokenException exception = assertThrows(TokenException.class, () -> {
            userTokenService.getTokenData("invalidToken");
        });

        assertEquals("Token not found.", exception.getMessage());

        verify(userTokenDao, times(1)).findUserTokenByToken(anyString());
    }

    @Test
    void testGetTokenDataWithEmptyToken() {
        TokenException exception = assertThrows(TokenException.class, () -> {
            userTokenService.getTokenData(null);
        });

        assertEquals("Token not found.", exception.getMessage());

        verify(userTokenDao, never()).findUserTokenByToken(anyString());
    }

    @Test
    void testDeleteTokenSuccess() {

        userTokenService.deleteToken(1L);

        verify(userTokenDao, times(1)).deleteTokenById(1L);
    }

    @Test
    void testDeleteTokenByUserIdSuccess() {

        userTokenService.deleteTokenByUserId(1L);

        verify(userTokenDao, times(1)).deleteTokensByUserId(1L);
    }

    @Test
    void testDeleteTokenByNonExistingUserId() {
        doThrow(new UserNotFoundException("User not found for id: 999", Map.of("id", 999L))).when(userTokenDao).deleteTokensByUserId(999L);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userTokenService.deleteTokenByUserId(999L);
        });

        assertEquals("User not found for id: 999", exception.getMessage());

        verify(userTokenDao, times(1)).deleteTokensByUserId(999L);
    }

}
