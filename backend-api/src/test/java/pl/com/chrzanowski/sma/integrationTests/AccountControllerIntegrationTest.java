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
import pl.com.chrzanowski.sma.auth.dto.request.UserEditPasswordChangeRequest;
import pl.com.chrzanowski.sma.auth.dto.request.UserEditRoleUpdateRequest;
import pl.com.chrzanowski.sma.auth.dto.request.UserUpdateRequest;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.email.service.SendEmailService;
import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;
import pl.com.chrzanowski.sma.role.mapper.RoleMapper;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.repository.RoleRepository;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.repository.UserRepository;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private SendEmailService sendEmailService;

    @Autowired
    private Flyway flyway;

    @Autowired
    private UserHelper userHelper;

    private String jwtToken;
    private User registeredUser;
    private LoginRequest firstUser;


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

        firstUser = userHelper.registerFirstUser(webTestClient);
        this.jwtToken = userHelper.authenticateUser(firstUser, webTestClient);
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
        assertThat(response.login()).isEqualTo("login");
    }

    @Test
    void shouldUpdateAccountSuccessfully() {
        registeredUser = userRepository.findByLogin(firstUser.getLogin()).get();
        Set<Role> roleList = registeredUser.getRoles();
        List<String> roleNames = new ArrayList<>();
        for (Role role : roleList) {
            roleNames.add(role.getName());
        }
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .id(registeredUser.getId())
                .login("newUsername")
                .email("newemail@test.com")
                .roles(roleNames)
                .build();

        webTestClient.put()
                .uri("/api/account/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userUpdateRequest), UserDTO.class)
                .exchange()
                .expectStatus().isOk();

        User updatedUser = userRepository.findById(registeredUser.getId()).orElseThrow();
        assertThat(updatedUser.getLogin()).isEqualTo("newUsername");
        assertThat(updatedUser.getEmail()).isEqualTo("newemail@test.com");
    }

    @Test
    void shouldFailUpdateAccountWithInvalidData() {
        registeredUser = userRepository.findAll().get(0);
        UserDTO invalidUserDTO = UserDTO.builder()
                .id(registeredUser.getId())
                .login("") // Invalid data: login cannot be empty
                .email("invalidemail")
                .build();

        webTestClient.put()
                .uri("/api/account/update")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(invalidUserDTO), UserDTO.class)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        registeredUser = userRepository.findAll().get(0);
        UserEditPasswordChangeRequest passwordChangeRequest = new UserEditPasswordChangeRequest(registeredUser.getId(), "password", "newPassword");

        webTestClient.put()
                .uri("/api/account/change-password")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(passwordChangeRequest), UserEditPasswordChangeRequest.class)
                .exchange()
                .expectStatus().isNoContent();

        User updatedUser = userRepository.findById(registeredUser.getId()).orElseThrow();
        assertFalse(passwordEncoder.matches(updatedUser.getPassword(), registeredUser.getPassword()));
    }

    @Test
    void shouldFailChangePasswordWithInvalidUserId() {
        UserEditPasswordChangeRequest invalidRequest = new UserEditPasswordChangeRequest(999L, "newPassword", "newPassword");

        webTestClient.put()
                .uri("/api/account/change-password")
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailChangePasswordWithInvalidPassword() {
        registeredUser = userRepository.findAll().get(0);
        UserEditPasswordChangeRequest invalidRequest = new UserEditPasswordChangeRequest(registeredUser.getId(), "", "newPassword");

        webTestClient.put()
                .uri("/api/account/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldUpdateRolesSuccessfully() {
        registeredUser = userRepository.findByLogin(firstUser.getLogin()).get();

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        UserEditRoleUpdateRequest userEditRoleUpdateRequest = new UserEditRoleUpdateRequest(registeredUser.getId(), List.of(roleMapper.toDto(userRole)));

        webTestClient.put()
                .uri("/api/account/update-roles")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userEditRoleUpdateRequest), UserEditRoleUpdateRequest.class)
                .exchange()
                .expectStatus().isNoContent();

        User updatedUser = userRepository.findByLogin(registeredUser.getLogin()).orElseThrow();
        Set<Role> roleSet = updatedUser.getRoles();
        assertThat(roleSet).isNotNull();
        assertThat(roleSet).doesNotContain(adminRole);
        assertThat(roleSet).contains(userRole);

    }
}
