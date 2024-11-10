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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.auth.dto.request.LoginRequest;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.response.JWTToken;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.email.service.SendEmailService;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.repository.RoleRepository;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.repository.UserRepository;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserControllerIntegrationTest extends AbstractTestContainers {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private Flyway flyway;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private SendEmailService sendEmailService;

    private String jwtToken;
    @Autowired
    private UserRepository userRepository;

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

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        RegisterRequest existingUser = RegisterRequest.builder()
                .username("username")
                .password("password")
                .email("username@test.com")
                .role(Set.of(adminRole.getName()))
                .build();

        registerUser(existingUser);
        RegisterRequest secondUser = RegisterRequest.builder()
                .username("second")
                .password("password")
                .email("second@test.com")
                .role(Set.of(userRole.getName()))
                .build();
        registerUser(secondUser);

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

    private void registerUser(RegisterRequest request) {


        String confirmationToken = webTestClient.post().uri("/api/auth/register")
                .body(Mono.just(request), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", confirmationToken).build())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldGetAllUsersSuccessfully() {
        List<UserDTO> users = webTestClient.get()
                .uri("/api/users/all")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isNotEmpty();
    }

    @Test
    void shouldGetUserByIdSuccessfully() {
        UserDTO userDTO = webTestClient.get()
                .uri("/api/users/getById/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(userDTO).isNotNull();
        assertThat(userDTO.getId()).isEqualTo(1L);
    }

    @Test
    void shouldAddUserSuccessfully() {
        UserDTO newUser = UserDTO.builder()
                .username("newuser")
                .email("newuser@test.com")
                .password("newpassword")
                .build();

        UserDTO savedUser = webTestClient.post()
                .uri("/api/users/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("newuser");
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        UserDTO updateUser = UserDTO.builder()
                .id(1L)
                .username("updateduser")
                .email("updated@test.com")
                .build();

        UserDTO updatedUser = webTestClient.put()
                .uri("/api/users/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo("updateduser");
    }

    @Test
    void shouldDeleteUserByIdSuccessfully() {
        webTestClient.delete()
                .uri("/api/users/delete/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk();

        List<Role> roles = roleRepository.findAll();
        List<User> users = userRepository.findAll();
        assertThat(roles).hasSize(3);
        assertThat(users).hasSize(1);
    }

    @Test
    void shouldFailGetUsersWithoutAuthentication() {
        webTestClient.get()
                .uri("/api/users/all")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldFailAddUserWithoutAuthentication() {
        UserDTO newUser = UserDTO.builder()
                .username("unauthorizeduser")
                .email("unauthorized@test.com")
                .password("password")
                .build();

        webTestClient.post()
                .uri("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newUser)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldFailDeleteUserWithoutAuthentication() {
        webTestClient.delete()
                .uri("/api/users/delete/1")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldGetUsersByFilterSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("usernameStartsWith", "user");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/")
                        .queryParams(queryParams).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isNotEmpty();
        assertThat(users).anyMatch(user -> "username".equals(user.getUsername()));
    }

    @Test
    void shouldGetUsersByFilterAndPageSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("usernameStartsWith", "username");
        queryParams.add("page", "0");
        queryParams.add("pageSize", "10");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/page")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isNotEmpty();
        assertThat(users).anyMatch(user -> "username".equals(user.getUsername()));
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersMatchFilter() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("usernameStartsWith", "nonexistentuser");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/").queryParams(queryParams).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isEmpty();
    }

    @Test
    void shouldFailGetUsersByFilterWithoutAuthentication() {
        webTestClient.get()
                .uri("/api/users/")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldFailGetUsersByFilterAndPageWithoutAuthentication() {
        webTestClient.get()
                .uri("/api/users/page?page=0&size=10")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldGetUsersByUsernameStartsWithSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("usernameStartsWith", "user");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
        assertEquals(users.get(0).getUsername(), "username");
    }

    @Test
    void shouldGetUsersByEmailStartsWithSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("emailStartsWith", "username");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isNotEmpty();
        assertThat(users).allMatch(user -> user.getEmail().startsWith("username"));
    }

    @Test
    void shouldGetUsersByFilterAndPageWithUsernameStartsWithSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("usernameStartsWith", "user");
        queryParams.add("page", "0");
        queryParams.add("pageSize", "10");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/page")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isNotEmpty();
        assertThat(users).allMatch(user -> user.getUsername().startsWith("user"));
    }

    @Test
    void shouldGetUsersByFilterAndPageWithEmailStartsWithSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("emailStartsWith", "user");
        queryParams.add("page", "0");
        queryParams.add("pageSize", "10");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/page")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isNotEmpty();
        assertThat(users).allMatch(user -> user.getEmail().startsWith("username"));
    }
}
