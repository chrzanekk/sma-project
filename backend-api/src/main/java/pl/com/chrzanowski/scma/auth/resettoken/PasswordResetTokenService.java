package pl.com.chrzanowski.scma.auth.resettoken;

import pl.com.chrzanowski.scma.user.UserDTO;

public interface PasswordResetTokenService {

    String generate();

    PasswordResetTokenDTO save(String token, UserDTO userDTO);

    PasswordResetTokenDTO update(PasswordResetTokenDTO passwordResetTokenDTO);

    PasswordResetTokenDTO get(String token);

    void delete(Long id);
}
