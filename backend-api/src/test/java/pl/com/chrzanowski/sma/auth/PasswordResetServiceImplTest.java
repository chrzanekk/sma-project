package pl.com.chrzanowski.sma.auth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.com.chrzanowski.sma.auth.dto.request.NewPasswordPutRequest;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.auth.service.PasswordResetServiceImpl;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;
import pl.com.chrzanowski.sma.usertoken.service.UserTokenService;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PasswordResetServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserTokenService userTokenService;

    @InjectMocks
    private PasswordResetServiceImpl passwordResetService;

    private UserDTO userDTO;
    private UserTokenDTO userTokenDTO;
    private NewPasswordPutRequest request;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
       autoCloseable = MockitoAnnotations.openMocks(this);

        userDTO = UserDTO.builder()
                .id(1L)
                .email("test@example.com")
                .password("oldPassword")
                .build();


        userTokenDTO = UserTokenDTO.builder()
                .email("test@example.com")
                .expireDate(LocalDateTime.now().plusDays(1))
                .build();

        request = new NewPasswordPutRequest("newPassword","newPassword","veryVeryLongToken");
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSaveNewPasswordSuccess() {
        when(userService.getUser(anyString())).thenReturn(userDTO);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");
        when(userService.save(any(UserDTO.class))).thenReturn(userDTO.toBuilder().password("encodedNewPassword").build());
        userTokenService.updateToken(any(UserTokenDTO.class));

        MessageResponse result = passwordResetService.saveNewPassword(userTokenDTO, request);

        assertNotNull(result);
        assertEquals("Password changed successfully.", result.getMessage());

        verify(userService, times(1)).getUser(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userService, times(1)).save(any(UserDTO.class));
        verify(userTokenService, times(1)).updateToken(any(UserTokenDTO.class));
    }

    @Test
    void testSaveNewPasswordUserNotFound() {
        when(userService.getUser(anyString())).thenThrow(new IllegalArgumentException("User not found"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            passwordResetService.saveNewPassword(userTokenDTO, request);
        });

        assertEquals("User not found", exception.getMessage());

        verify(userService, times(1)).getUser(anyString());
        verify(passwordEncoder, times(0)).encode(anyString());  // Nie powinno zakodować hasła
        verify(userService, times(0)).save(any(UserDTO.class)); // Nie powinno zapisać użytkownika
        verify(userTokenService, times(0)).updateToken(any(UserTokenDTO.class)); // Nie powinno zaktualizować tokenu
    }

    @Test
    void testSaveNewPasswordTokenUpdateFailed() {
        when(userService.getUser(anyString())).thenReturn(userDTO);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");
        when(userService.save(any(UserDTO.class))).thenReturn(userDTO.toBuilder().password("encodedNewPassword").build());
        doThrow(new IllegalStateException("Token update failed")).when(userTokenService).updateToken(any(UserTokenDTO.class));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            passwordResetService.saveNewPassword(userTokenDTO, request);
        });

        assertEquals("Token update failed", exception.getMessage());

        verify(userService, times(1)).getUser(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userService, times(1)).save(any(UserDTO.class));
        verify(userTokenService, times(1)).updateToken(any(UserTokenDTO.class)); // Powinna próbować zaktualizować token
    }
}
