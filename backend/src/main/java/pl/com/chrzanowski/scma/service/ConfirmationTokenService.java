package pl.com.chrzanowski.scma.service;

import pl.com.chrzanowski.scma.service.dto.ConfirmationTokenDTO;
import pl.com.chrzanowski.scma.service.dto.UserDTO;

public interface ConfirmationTokenService {

    String generateToken();

    ConfirmationTokenDTO saveToken(String token, UserDTO userDTO);

    ConfirmationTokenDTO updateToken(ConfirmationTokenDTO confirmationTokenDTO);

    ConfirmationTokenDTO getConfirmationToken(String token);

    void deleteConfirmationToken(Long id);
}
