package pl.com.chrzanowski.sma.auth.dto.request;

public record NewPasswordPutRequest(
        String password,
        String confirmPassword,
        String token) {
}
