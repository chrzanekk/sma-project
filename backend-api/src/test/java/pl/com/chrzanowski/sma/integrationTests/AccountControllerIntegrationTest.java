package pl.com.chrzanowski.sma.integrationTests;


import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.auth.dto.request.LoginRequest;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.response.JWTToken;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.email.service.SendEmailService;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.dto.UserPasswordChangeRequest;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.repository.UserRepository;
import pl.com.chrzanowski.sma.usertoken.repository.UserTokenRepository;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
public class AccountControllerIntegrationTest extends AbstractTestContainers {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private SendEmailService sendEmailService;

    @Autowired
    private Flyway flyway;

    private String jwtToken;
    private User registeredUser;


    @BeforeEach
    void setUp() {
        this.webTestClient = this.webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(60)).build();
        flyway.clean();
        flyway.migrate();

        reset(sendEmailService);

        when(sendEmailService.sendAfterRegistration(any(), any()))
                .thenReturn(new MessageResponse("Register successful"));
        when(sendEmailService.sendAfterEmailConfirmation(any(), any()))
                .thenReturn(new MessageResponse("Confirmed successful"));
        when(sendEmailService.sendPasswordResetMail(any(), any()))
                .thenReturn(new MessageResponse("Password reset token sent"));
        when(sendEmailService.sendAfterPasswordChange(any(), any()))
                .thenReturn(new MessageResponse("Password changed successfully"));

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
        this.jwtToken = result.getTokenValue();
    }

    @Test
    void shouldGetAccountSuccessfully() {

        UserInfoResponse response = webTestClient.get()
                .uri("/api/account/get")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserInfoResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo("username");
    }

    @Test
    void shouldUpdateAccountSuccessfully() {
        registeredUser = userRepository.findAll().get(0);
        UserDTO userDTO = UserDTO.builder()
                .id(registeredUser.getId())
                .username("newUsername")
                .email("newemail@test.com")
                .build();

        webTestClient.put()
                .uri("/api/account/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userDTO), UserDTO.class)
                .exchange()
                .expectStatus().isOk();

        User updatedUser = userRepository.findById(registeredUser.getId()).orElseThrow();
        assertThat(updatedUser.getUsername()).isEqualTo("newUsername");
        assertThat(updatedUser.getEmail()).isEqualTo("newemail@test.com");
    }

    @Test
    void shouldFailUpdateAccountWithInvalidData() {
        registeredUser = userRepository.findAll().get(0);
        UserDTO invalidUserDTO = UserDTO.builder()
                .id(registeredUser.getId())
                .username("") // Invalid data: username cannot be empty
                .email("invalidemail")
                .build();

        webTestClient.put()
                .uri("/api/account/update")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(invalidUserDTO), UserDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        registeredUser = userRepository.findAll().get(0);
        UserPasswordChangeRequest passwordChangeRequest = new UserPasswordChangeRequest(registeredUser.getId(), "password", "newPassword");

        webTestClient.put()
                .uri("/api/account/change-password")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(passwordChangeRequest), UserPasswordChangeRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

        User updatedUser = userRepository.findById(registeredUser.getId()).orElseThrow();
        assertFalse(passwordEncoder.matches(updatedUser.getPassword(), registeredUser.getPassword()));
    }

    @Test
    void shouldFailChangePasswordWithInvalidUserId() {
        UserPasswordChangeRequest invalidRequest = new UserPasswordChangeRequest(999L, "newPassword", "newPassword");

        webTestClient.put()
                .uri("/api/account/change-password")
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldFailChangePasswordWithInvalidPassword() {
        registeredUser = userRepository.findAll().get(0);
        UserPasswordChangeRequest invalidRequest = new UserPasswordChangeRequest(registeredUser.getId(), "", "newPassword");

        webTestClient.put()
                .uri("/api/account/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
