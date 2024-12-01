package pl.com.chrzanowski.sma.auth.dto.request;

public record UserPasswordChangeRequest(Long userId,
                                        String password,
                                        String newPassword) {
}
