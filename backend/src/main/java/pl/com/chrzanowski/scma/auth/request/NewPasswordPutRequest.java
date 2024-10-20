package pl.com.chrzanowski.scma.auth.request;

public record NewPasswordPutRequest(
        String password,
        String confirmPassword,
        String token) {
}
