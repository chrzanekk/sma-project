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
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.email.service.SendEmailService;
import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.service.RoleService;

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

    @Autowired
    private UserHelper userHelper;

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

        LoginRequest firstUser = userHelper.registerFirstUser(webTestClient);
        this.jwtToken = userHelper.authenticateUser(firstUser, webTestClient);
    }

    @Test
    void shouldGetRolesSuccessfully() {
        RoleDTO roleDTO = RoleDTO.builder()
                .name("ROLE_TEST")
                .build();
        roleService.saveRole(roleDTO);

        List<RoleDTO> roles = webTestClient.get()
                .uri("/api/roles/all")
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
                .expectStatus().isOk()
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

        Boolean response = webTestClient.delete()
                .uri("/api/roles/delete/" + savedRole.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).isTrue();
    }

    @Test
    void shouldReturnNoContentWhenDeletingNonExistentRole() {
        webTestClient.delete()
                .uri("/api/roles/delete/999")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldFailGetRolesWithoutAuthentication() {
        webTestClient.get()
                .uri("/api/roles/all")
                .exchange()
                .expectStatus().isUnauthorized();
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
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailDeleteRoleWithoutAuthentication() {
        webTestClient.delete()
                .uri("/api/roles/delete/1")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
