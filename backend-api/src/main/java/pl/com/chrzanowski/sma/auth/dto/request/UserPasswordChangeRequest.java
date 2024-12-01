package pl.com.chrzanowski.sma.auth.dto.request;

public record UserPasswordChangeRequest(Long userId,
                                        String currentPassword,
                                        String newPassword) {
}
