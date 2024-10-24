package pl.com.chrzanowski.sma.auth.usertokens;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.enumeration.TokenType;
import pl.com.chrzanowski.sma.common.exception.ObjectNotFoundException;
import pl.com.chrzanowski.sma.user.UserDTO;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserTokenServiceImplTest {

    @Mock
    private UserTokenRepository userTokenRepository;

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
                .username("testUser")
                .build();

        userTokenDTO = UserTokenDTO.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .userId(1L)
                .userName("testUser")
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
        when(userTokenRepository.save(any(UserToken.class))).thenReturn(userToken);
        when(userTokenMapper.toDto(any(UserToken.class))).thenReturn(userTokenDTO);

        UserTokenDTO result = userTokenService.saveToken(userTokenDTO.getToken(), userDTO, TokenType.PASSWORD_RESET_TOKEN);

        assertNotNull(result);
        assertEquals(userTokenDTO.getToken(), result.getToken());

        verify(userTokenMapper, times(1)).toEntity(any(UserTokenDTO.class));
        verify(userTokenRepository, times(1)).save(any(UserToken.class));
        verify(userTokenMapper, times(1)).toDto(any(UserToken.class));
    }

    @Test
    void testSaveTokenUserNull() {
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            userTokenService.saveToken(userTokenDTO.getToken(), null, TokenType.PASSWORD_RESET_TOKEN);
        });

        assertEquals("User must not be null", exception.getMessage());

        verify(userTokenRepository, times(0)).save(any(UserToken.class));
    }

    @Test
    void testUpdateTokenSuccess() {
        when(userTokenMapper.toEntity(any(UserTokenDTO.class))).thenReturn(userToken);
        when(userTokenRepository.save(any(UserToken.class))).thenReturn(userToken);
        when(userTokenMapper.toDto(any(UserToken.class))).thenReturn(userTokenDTO);

        UserTokenDTO result = userTokenService.updateToken(userTokenDTO);

        assertNotNull(result);
        assertEquals(userTokenDTO.getToken(), result.getToken());

        verify(userTokenMapper, times(1)).toEntity(any(UserTokenDTO.class));
        verify(userTokenRepository, times(1)).save(any(UserToken.class));
        verify(userTokenMapper, times(1)).toDto(any(UserToken.class));
    }

    @Test
    void testGetTokenDataSuccess() {
        when(userTokenRepository.findUserTokensByToken(anyString())).thenReturn(Optional.of(userToken));
        when(userTokenMapper.toDto(any(UserToken.class))).thenReturn(userTokenDTO);

        UserTokenDTO result = userTokenService.getTokenData(userTokenDTO.getToken());

        assertNotNull(result);
        assertEquals(userTokenDTO.getToken(), result.getToken());

        verify(userTokenRepository, times(1)).findUserTokensByToken(anyString());
        verify(userTokenMapper, times(1)).toDto(any(UserToken.class));
    }

    @Test
    void testGetTokenDataNotFound() {
        when(userTokenRepository.findUserTokensByToken(anyString())).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            userTokenService.getTokenData("invalidToken");
        });

        assertEquals("Token not found.", exception.getMessage());

        verify(userTokenRepository, times(1)).findUserTokensByToken(anyString());
    }

    @Test
    void testDeleteConfirmationTokenSuccess() {
        userTokenService.deleteConfirmationToken(1L);

        verify(userTokenRepository, times(1)).deleteById(1L);
    }
}
