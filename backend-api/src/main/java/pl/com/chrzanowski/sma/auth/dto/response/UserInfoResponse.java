package pl.com.chrzanowski.sma.auth.dto.response;

import java.util.List;

public record UserInfoResponse(
        Long id,
        String login,
        String email,
        List<String> roles
) {
}
