package pl.com.chrzanowski.sma.integrationTests.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.com.chrzanowski.sma.auth.dto.request.LoginRequest;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.response.JWTToken;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.repository.RoleRepository;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.repository.UserRepository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Component
public class UserHelper {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public LoginRequest registerFirstUser(WebTestClient webTestClient) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found"));


        RegisterRequest existingUser = RegisterRequest.builder()
                .login("login")
                .password("password")
                .email("login@test.com")
                .firstName("firstName")
                .lastName("lastName")
                .position("firstPosition")
                .role(Set.of(adminRole.getName()))
                .build();
        registerUser(existingUser, webTestClient);

        activateUser("login");

        return LoginRequest.builder()
                .login("login")
                .password("password")
                .build();

    }

    public LoginRequest registerSecondUser(WebTestClient webTestClient) {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        RegisterRequest secondUser = RegisterRequest.builder()
                .login("second")
                .password("password")
                .email("second@test.com")
                .firstName("secondFirstName")
                .lastName("secondLastName")
                .position("secondPosition")
                .role(Set.of(userRole.getName()))
                .build();
        registerUser(secondUser, webTestClient);

        activateUser("second");
        return LoginRequest.builder()
                .login("second")
                .password("password")
                .build();

    }


    private void registerUser(RegisterRequest request, WebTestClient client) {
        String confirmationToken = client.post().uri("/api/auth/register")
                .body(Mono.just(request), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseHeaders().getFirst("Confirmation-Token");

        client.get()
                .uri(uriBuilder -> uriBuilder.path("/api/auth/confirm").queryParam("token", confirmationToken).build())
                .exchange()
                .expectStatus().isOk();
    }

    public String authenticateUser(LoginRequest loginRequest, WebTestClient client) {
        JWTToken token = client.post()
                .uri("/api/auth/login")
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(JWTToken.class)
                .returnResult().getResponseBody();

        assert token != null;
        return token.getTokenValue() != null ? token.getTokenValue() : "";
    }

    public void activateUser(String userLogin) {
        User user = userRepository.findByLogin(userLogin).get();
        UserDTO userDTO = userMapper.toDto(user);
        UserDTO updatedUser = userDTO.toBuilder().locked(false).build();
        userRepository.save(userMapper.toEntity(updatedUser));
        userRepository.flush();
    }
}
