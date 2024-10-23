package pl.com.chrzanowski.sma.auth.request;

public record NewPasswordPutRequest(
        String password,
        String confirmPassword,
        String token) {
}
