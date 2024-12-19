package pl.com.chrzanowski.sma.user.dto;

public record AdminEditPasswordChangeRequest(
        Long userId,
        String newPassword
) {
}
