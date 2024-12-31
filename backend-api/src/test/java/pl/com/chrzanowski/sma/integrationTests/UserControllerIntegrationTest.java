package pl.com.chrzanowski.sma.integrationTests;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.auth.dto.request.LoginRequest;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.email.service.SendEmailService;
import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.repository.RoleRepository;
import pl.com.chrzanowski.sma.user.dao.UserDao;
import pl.com.chrzanowski.sma.user.dto.AdminEditPasswordChangeRequest;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.repository.UserRepository;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String jwtToken;

    private User firstUser;

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

        LoginRequest firstUserLogin = userHelper.registerFirstUser(webTestClient);
        userHelper.registerSecondUser(webTestClient);
        this.jwtToken = userHelper.authenticateUser(firstUserLogin, webTestClient);
        firstUser = userDao.findByLogin(firstUserLogin.getLogin()).get();
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
        assertThat(users.size()).isEqualTo(2);
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
                .login("newuser")
                .email("newuser@test.com")
                .password("newpassword")
                .firstName("firstName")
                .lastName("lastName")
                .position("position")
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
        assertThat(savedUser.getLogin()).isEqualTo("newuser");
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        UserDTO updateUser = UserDTO.builder()
                .id(1L)
                .login("updateduser")
                .email("updated@test.com")
                .firstName(firstUser.getFirstName())
                .lastName(firstUser.getLastName())
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
        assertThat(updatedUser.getLogin()).isEqualTo("updateduser");
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
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailAddUserWithoutAuthentication() {
        UserDTO newUser = UserDTO.builder()
                .login("unauthorizeduser")
                .email("unauthorized@test.com")
                .password("password")
                .build();

        webTestClient.post()
                .uri("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newUser)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailDeleteUserWithoutAuthentication() {
        webTestClient.delete()
                .uri("/api/users/delete/1")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldGetUsersByLoginFilterSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("loginStartsWith", "log");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/")
                        .queryParams(queryParams).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users).anyMatch(user -> "login".equals(user.getLogin()));
    }

    @Test
    void shouldGetUsersByEmailFilterSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("emailStartsWith", "log");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/")
                        .queryParams(queryParams).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users).anyMatch(user -> "login".equals(user.getLogin()));
    }

    @Test
    void shouldGetUsersByFirstNameFilterSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("firstNameStartsWith", "first");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/")
                        .queryParams(queryParams).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users).anyMatch(user -> "login".equals(user.getLogin()));
    }

    @Test
    void shouldGetUsersByLastNameFilterSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("lastNameStartsWith", "last");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/")
                        .queryParams(queryParams).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users).anyMatch(user -> "login".equals(user.getLogin()));
    }

    @Test
    void shouldGetUsersByPositionFilterSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("positionStartsWith", "firstPos");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/")
                        .queryParams(queryParams).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users).anyMatch(user -> "login".equals(user.getLogin()));
    }

    @Test
    void shouldGetUsersByFilterAndPageSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("loginStartsWith", "login");
        queryParams.add("page", "0");
        queryParams.add("pageSize", "10");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/page")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.numberOfElements").isEqualTo(1)
                .jsonPath("$.content[0].id").isNotEmpty()
                .jsonPath("$.content[0].login").isEqualTo("login");
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersMatchFilter() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("loginStartsWith", "nonexistentuser");

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
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldFailGetUsersByFilterAndPageWithoutAuthentication() {
        webTestClient.get()
                .uri("/api/users/page?page=0&size=10")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldGetUsersByLoginStartsWithSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("loginStartsWith", "log");

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
        assertEquals(users.get(0).getLogin(), "login");
    }

    @Test
    void shouldGetUsersByEmailStartsWithSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("emailStartsWith", "login");

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
        assertThat(users).allMatch(user -> user.getEmail().startsWith("login"));
    }

    @Test
    void shouldGetUsersByFilterAndPageWithLoginStartsWithSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("loginStartsWith", "log");
        queryParams.add("page", "0");
        queryParams.add("pageSize", "10");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/page")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.numberOfElements").isEqualTo(1)
                .jsonPath("$.content[0].id").isNotEmpty()
                .jsonPath("$.content[0].login").isEqualTo("login");
    }

    @Test
    void shouldGetUsersByFilterAndPageWithEmailStartsWithSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("emailStartsWith", "log");
        queryParams.add("page", "0");
        queryParams.add("pageSize", "10");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/page")
                        .queryParams(queryParams)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.numberOfElements").isEqualTo(1)
                .jsonPath("$.content[0].id").isNotEmpty()
                .jsonPath("$.content[0].login").isEqualTo("login");
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        firstUser = userRepository.findAll().get(0);
        AdminEditPasswordChangeRequest passwordChangeRequest = new AdminEditPasswordChangeRequest(firstUser.getId(), "newPassword");

        webTestClient.put()
                .uri("/api/users/set-new-password")
                .header(org.testcontainers.shaded.com.google.common.net.HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(passwordChangeRequest), AdminEditPasswordChangeRequest.class)
                .exchange()
                .expectStatus().isOk();

        User updatedUser = userRepository.findById(firstUser.getId()).orElseThrow();
        assertFalse(passwordEncoder.matches(updatedUser.getPassword(), firstUser.getPassword()));
    }

    @Test
    void shouldGetUsersByRolesSuccessfully() {
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("roles", "ROLE_ADMIN");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/")
                        .queryParams(queryParams).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users).anyMatch(user -> user.getLogin().equals("login"));
    }

    @Test
    void shouldGetUsersByMultipleRolesSuccessfully() {
        // Dodaj role USER i ADMIN do bazy danych
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();

        // Filtrowanie po rolach ADMIN i USER
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("roles", "ROLE_ADMIN");
        queryParams.add("roles", "ROLE_USER");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/")
                        .queryParams(queryParams).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(2);
        assertThat(users).anyMatch(user -> user.getLogin().equals("login"));
        assertThat(users).anyMatch(user -> user.getLogin().equals("second"));
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersMatchRoles() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("roles", "ROLE_NON_EXISTENT");

        List<UserDTO> users = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/")
                        .queryParams(queryParams).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .returnResult().getResponseBody();

        assertThat(users).isEmpty();
    }

}
