package pl.com.chrzanowski.sma.integrationTests;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.auth.dto.request.LoginRequest;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.response.JWTToken;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.email.service.SendEmailService;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.service.RoleService;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
public class RoleControllerIntegrationTest extends AbstractTestContainers {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RoleService roleService;

    @MockBean
    private SendEmailService sendEmailService;

    @Autowired
    private Flyway flyway;

    private String jwtToken;

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
    void shouldGetRolesSuccessfully() {
        RoleDTO roleDTO = RoleDTO.builder()
                .name("ROLE_TEST")
                .build();
        roleService.saveRole(roleDTO);

        List<RoleDTO> roles = webTestClient.get()
                .uri("/api/roles")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RoleDTO.class)
                .returnResult().getResponseBody();

        assertThat(roles).isNotEmpty();
        assertThat(roles).anyMatch(role -> "ROLE_TEST".equals(role.getName()));
    }

    @Test
    void shouldSaveRoleSuccessfully() {
        RoleDTO roleDTO = RoleDTO.builder()
                .name("ROLE_TEST")
                .build();

        RoleDTO savedRole = webTestClient.post()
                .uri("/api/roles/save")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roleDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(RoleDTO.class)
                .returnResult().getResponseBody();

        assertThat(savedRole).isNotNull();
        assertThat(savedRole.getName()).isEqualTo("ROLE_TEST");
    }

    @Test
    void shouldDeleteRoleSuccessfully() {
        RoleDTO roleDTO = RoleDTO.builder()
                .name("ROLE_TO_DELETE")
                .build();
        RoleDTO savedRole = roleService.saveRole(roleDTO);

        MessageResponse response = webTestClient.delete()
                .uri("/api/roles/" + savedRole.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageResponse.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("Role deleted successfully");
    }

    @Test
    void shouldReturnNoContentWhenDeletingNonExistentRole() {
        webTestClient.delete()
                .uri("/api/roles/999")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldFailGetRolesWithoutAuthentication() {
        webTestClient.get()
                .uri("/api/roles")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldFailSaveRoleWithoutAuthentication() {
        RoleDTO roleDTO = RoleDTO.builder()
                .name("ROLE_TEST")
                .build();

        webTestClient.post()
                .uri("/api/roles/save")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roleDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldFailDeleteRoleWithoutAuthentication() {
        webTestClient.delete()
                .uri("/api/roles/1")
                .exchange()
                .expectStatus().isBadRequest();
    }
}
