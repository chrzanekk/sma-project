package pl.com.chrzanowski.scma.payload.request;

public record NewPasswordPutRequest(
        String password,
        String confirmPassword,
        String token) {
}
