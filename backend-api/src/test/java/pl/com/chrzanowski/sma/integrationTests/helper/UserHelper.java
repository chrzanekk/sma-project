package pl.com.chrzanowski.sma.integrationTests.helper;

import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.com.chrzanowski.sma.auth.dto.request.LoginRequest;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.response.JWTToken;
import reactor.core.publisher.Mono;

@Component
public class UserHelper {


    public static void registerUser(RegisterRequest request, WebTestClient client) {


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

    public static String authenticateUser(LoginRequest loginRequest, WebTestClient client) {
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
}
