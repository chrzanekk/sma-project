package pl.com.chrzanowski.sma.user.dto;

public record UserPasswordChangeRequest(Long userId,
                                        String currentPassword,
                                        String newPassword) {
}
