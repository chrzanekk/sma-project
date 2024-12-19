package pl.com.chrzanowski.sma.auth.dto.request;

public record UserEditPasswordChangeRequest(Long userId,
                                            String password,
                                            String newPassword) {
}
