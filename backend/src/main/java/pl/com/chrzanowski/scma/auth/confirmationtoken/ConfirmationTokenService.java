package pl.com.chrzanowski.scma.auth.confirmationtoken;

import pl.com.chrzanowski.scma.user.UserDTO;

public interface ConfirmationTokenService {

    String generateToken();

    ConfirmationTokenDTO saveToken(String token, UserDTO userDTO);

    ConfirmationTokenDTO updateToken(ConfirmationTokenDTO confirmationTokenDTO);

    ConfirmationTokenDTO getConfirmationToken(String token);

    void deleteConfirmationToken(Long id);
}
