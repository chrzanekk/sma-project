package pl.com.chrzanowski.sma.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank
    private String login;

    @NotBlank
    private String password;

    private Boolean rememberMe;
}
