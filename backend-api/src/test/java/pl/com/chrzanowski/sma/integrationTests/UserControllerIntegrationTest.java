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
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.email.service.SendEmailService;
import pl.com.chrzanowski.sma.integrationTests.helper.UserHelper;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.repository.RoleRepository;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.repository.UserRepository;

import java.time.Duration;
import java.util.List;

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

    @Autowired
    private UserRepository userRepository;

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
        userHelper.registerSecondUser(webTestClient);
        this.jwtToken = userHelper.authenticateUser(firstUser, webTestClient);
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
                .expectStatus().isBadRequest();
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
        assertThat(users.size()).isEqualTo(1);
        assertThat(users).anyMatch(user -> "login".equals(user.getLogin()));
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
        assertThat(users.size()).isEqualTo(1);
        assertThat(users).allMatch(user -> user.getLogin().startsWith("log"));
    }

    @Test
    void shouldGetUsersByFilterAndPageWithEmailStartsWithSuccessfully() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("emailStartsWith", "log");
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
        assertThat(users.size()).isEqualTo(1);
        assertThat(users).allMatch(user -> user.getEmail().startsWith("login"));
    }
}
