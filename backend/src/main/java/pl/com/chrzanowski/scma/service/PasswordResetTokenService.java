package pl.com.chrzanowski.scma.service;

import pl.com.chrzanowski.scma.service.dto.PasswordResetTokenDTO;
import pl.com.chrzanowski.scma.service.dto.UserDTO;

public interface PasswordResetTokenService {

    String generate();

    PasswordResetTokenDTO save(String token, UserDTO userDTO);

    PasswordResetTokenDTO update(PasswordResetTokenDTO passwordResetTokenDTO);

    PasswordResetTokenDTO get(String token);

    void delete(Long id);
}
