package pl.com.chrzanowski.sma.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.auth.dto.request.LoginRequest;
import pl.com.chrzanowski.sma.auth.dto.request.NewPasswordPutRequest;
import pl.com.chrzanowski.sma.auth.dto.request.PasswordResetRequest;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.response.JWTToken;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.email.service.SendEmailService;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.repository.UserRepository;
import pl.com.chrzanowski.sma.usertoken.repository.UserTokenRepository;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class AuthControllerIntegrationTest extends AbstractTestContainers {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @MockBean
    private SendEmailService sendEmailService;

    @BeforeEach
    void setUp() {
        this.webTestClient = this.webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(60)).build();

        reset(sendEmailService);

        when(sendEmailService.sendAfterRegistration(any(), any()))
                .thenReturn(new MessageResponse("Register successful"));
        when(sendEmailService.sendAfterEmailConfirmation(any(), any()))
                .thenReturn(new MessageResponse("Confirmed successful"));
        when(sendEmailService.sendPasswordResetMail(any(), any()))
                .thenReturn(new MessageResponse("Password reset token sent"));
        when(sendEmailService.sendAfterPasswordChange(any(), any()))
                .thenReturn(new MessageResponse("Password changed successfully"));
    }

    @Test
    void shouldRegisterSuccessfully() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        List<User> users = userRepository.findAll();
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        MessageResponse response = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(registerRequest), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseBody();
        assert response != null;
        assertThat(response.getMessage()).isEqualTo("Register successful");

        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
    }

    @Test
    void shouldRegisterFailWithBadUsername() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        String existingResponse = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(existingUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", existingResponse).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class);

        RegisterRequest newUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username2@test.com")
                .build();

        webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(newUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
        verify(sendEmailService, times(1)).sendAfterEmailConfirmation(any(), any());
    }

    @Test
    void shouldRegisterFailWithBadEmail() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        String existingResponse = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(existingUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", existingResponse).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class);

        RegisterRequest newUser = RegisterRequest.builder()
                .username("username2")
                .password("password")
                .email("username@test.com")
                .build();

        webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(newUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
        verify(sendEmailService, times(1)).sendAfterEmailConfirmation(any(), any());
    }

    @Test
    void shouldConfirmSuccessfully() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        String existingResponse = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(existingUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", existingResponse).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class);

        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
        verify(sendEmailService, times(1)).sendAfterEmailConfirmation(any(), any());
    }

    @Test
    void shouldConfirmFailWithBadToken() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        String existingResponse = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(existingUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        String badToken = existingResponse + existingResponse;

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", badToken).build())
                .exchange()
                .expectStatus().isBadRequest();

        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
    }

    @Test
    void shouldLoginSuccessfully() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        String existingResponse = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(existingUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", existingResponse).build())
                .exchange()
                .expectStatus().isOk();

        LoginRequest loginRequest = LoginRequest.builder()
                .username(existingUser.getUsername())
                .password(existingUser.getPassword())
                .rememberMe(false).build();

        JWTToken result = webTestClient.post()
                .uri("/api/auth/login")
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(JWTToken.class)
                .returnResult().getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.getTokenValue()).isGreaterThan("");

        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
        verify(sendEmailService, times(1)).sendAfterEmailConfirmation(any(), any());
    }

    @Test
    void shouldLoginFailWithBadPassword() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        String existingResponse = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(existingUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", existingResponse).build())
                .exchange()
                .expectStatus().isOk();

        LoginRequest loginRequest = LoginRequest.builder()
                .username(existingUser.getUsername())
                .password(existingUser.getPassword() + "999")
                .rememberMe(false).build();

        webTestClient.post()
                .uri("/api/auth/login")
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
        verify(sendEmailService, times(1)).sendAfterEmailConfirmation(any(), any());
    }

    @Test
    void shouldLoginFailWithBadLogin() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        String existingResponse = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(existingUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", existingResponse).build())
                .exchange()
                .expectStatus().isOk();

        LoginRequest loginRequest = LoginRequest.builder()
                .username(existingUser.getUsername() + "222")
                .password(existingUser.getPassword())
                .rememberMe(false).build();

        webTestClient.post()
                .uri("/api/auth/login")
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
        verify(sendEmailService, times(1)).sendAfterEmailConfirmation(any(), any());
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        String existingResponse = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(existingUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", existingResponse).build())
                .exchange()
                .expectStatus().isOk();

        LoginRequest loginRequest = LoginRequest.builder()
                .username(existingUser.getUsername())
                .password(existingUser.getPassword())
                .rememberMe(false).build();

        String token = webTestClient.post()
                .uri("/api/auth/login")
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseHeaders().getFirst("Authorization");

        Boolean result = webTestClient.get()
                .uri("/api/auth/authenticate")
                .header("Authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .returnResult().getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(true);

        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
        verify(sendEmailService, times(1)).sendAfterEmailConfirmation(any(), any());
    }

    @Test
    void shouldAuthenticateFailWithInvalidToken() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        String existingResponse = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(existingUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", existingResponse).build())
                .exchange()
                .expectStatus().isOk();

        LoginRequest loginRequest = LoginRequest.builder()
                .username(existingUser.getUsername())
                .password(existingUser.getPassword())
                .rememberMe(false).build();

        String token = webTestClient.post()
                .uri("/api/auth/login")
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseHeaders().getFirst("Authorization");

        webTestClient.get()
                .uri("/api/auth/authenticate")
                .header(HttpHeaders.AUTHORIZATION, token + "111")
                .exchange()
                .expectStatus().isBadRequest();

        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
        verify(sendEmailService, times(1)).sendAfterEmailConfirmation(any(), any());
    }

    @Test
    void shouldRequestResetPasswordSuccessfully() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        String existingResponse = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(existingUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", existingResponse).build())
                .exchange()
                .expectStatus().isOk();

        PasswordResetRequest passwordResetRequest = new PasswordResetRequest(existingUser.getEmail());

        MessageResponse result = webTestClient.put()
                .uri("/api/auth/request-password-reset")
                .body(Mono.just(passwordResetRequest), PasswordResetRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("Password reset token sent");

        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
        verify(sendEmailService, times(1)).sendAfterEmailConfirmation(any(), any());
        verify(sendEmailService, times(1)).sendPasswordResetMail(any(), any());
    }

    @Test
    void shouldRequestResetPasswordFailWithWrongEmail() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        String existingResponse = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(existingUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", existingResponse).build())
                .exchange()
                .expectStatus().isOk();

        PasswordResetRequest passwordResetRequest = new PasswordResetRequest("test@test.test");

        webTestClient.put()
                .uri("/api/auth/request-password-reset")
                .body(Mono.just(passwordResetRequest), PasswordResetRequest.class)
                .exchange()
                .expectStatus().isBadRequest();


        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
        verify(sendEmailService, times(1)).sendAfterEmailConfirmation(any(), any());
        verify(sendEmailService, times(0)).sendPasswordResetMail(any(), any());
    }


    @Test
    void shouldResetPasswordSuccessfully() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        String existingResponse = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(existingUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", existingResponse).build())
                .exchange()
                .expectStatus().isOk();

        PasswordResetRequest passwordResetRequest = new PasswordResetRequest(existingUser.getEmail());

        String result = webTestClient.put()
                .uri("/api/auth/request-password-reset")
                .body(Mono.just(passwordResetRequest), PasswordResetRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseHeaders().getFirst("Reset-Token");

        NewPasswordPutRequest newPasswordPutRequest = new NewPasswordPutRequest("newPassword", "newPassword", result);

        MessageResponse resultAfterReset = webTestClient.put()
                .uri("/api/auth/reset-password")
                .body(Mono.just(newPasswordPutRequest), NewPasswordPutRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseBody();

        assertThat(resultAfterReset).isNotNull();
        assertThat(resultAfterReset.getMessage()).isEqualTo("Password changed successfully");

        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
        verify(sendEmailService, times(1)).sendAfterEmailConfirmation(any(), any());
        verify(sendEmailService, times(1)).sendPasswordResetMail(any(), any());
        verify(sendEmailService, times(1)).sendAfterPasswordChange(any(), any());
    }

    @Test
    void shouldResetPasswordFailWithInvalidToken() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        String existingResponse = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(existingUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", existingResponse).build())
                .exchange()
                .expectStatus().isOk();

        PasswordResetRequest passwordResetRequest = new PasswordResetRequest(existingUser.getEmail());

        String result = webTestClient.put()
                .uri("/api/auth/request-password-reset")
                .body(Mono.just(passwordResetRequest), PasswordResetRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseHeaders().getFirst("Reset-Token");

        NewPasswordPutRequest newPasswordPutRequest = new NewPasswordPutRequest("newPassword", "newPassword", result + "111");

        webTestClient.put()
                .uri("/api/auth/reset-password")
                .body(Mono.just(newPasswordPutRequest), NewPasswordPutRequest.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
        verify(sendEmailService, times(1)).sendAfterEmailConfirmation(any(), any());
        verify(sendEmailService, times(1)).sendPasswordResetMail(any(), any());
        verify(sendEmailService, times(0)).sendAfterPasswordChange(any(), any());
    }

    @Test
    void shouldResetPasswordFailWithEmptyToken() {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .build();

        String existingResponse = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(existingUser), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", existingResponse).build())
                .exchange()
                .expectStatus().isOk();

        NewPasswordPutRequest newPasswordPutRequest = new NewPasswordPutRequest("newPassword", "newPassword", "");

        webTestClient.put()
                .uri("/api/auth/reset-password")
                .body(Mono.just(newPasswordPutRequest), NewPasswordPutRequest.class)
                .exchange()
                .expectStatus().isBadRequest();

        verify(sendEmailService, times(1)).sendAfterRegistration(any(), any());
        verify(sendEmailService, times(1)).sendAfterEmailConfirmation(any(), any());
        verify(sendEmailService, times(0)).sendPasswordResetMail(any(), any());
        verify(sendEmailService, times(0)).sendAfterPasswordChange(any(), any());
    }
}