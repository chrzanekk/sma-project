package pl.com.chrzanowski.scma.auth.resettoken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.scma.auth.request.NewPasswordPutRequest;
import pl.com.chrzanowski.scma.auth.response.MessageResponse;
import pl.com.chrzanowski.scma.user.UserService;
import pl.com.chrzanowski.scma.user.UserDTO;

import java.time.LocalDateTime;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final Logger log = LoggerFactory.getLogger(PasswordResetServiceImpl.class);

    private final PasswordResetTokenService passwordResetTokenService;
    private final UserService userService;
    private final PasswordEncoder encoder;

    public PasswordResetServiceImpl(PasswordResetTokenService passwordResetTokenService, UserService userService,
                                    PasswordEncoder encoder) {
        this.passwordResetTokenService = passwordResetTokenService;
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public MessageResponse saveNewPassword(PasswordResetTokenDTO passwordResetTokenDTO, NewPasswordPutRequest request) {
        log.debug("Request to save new password.");
        UserDTO userDTO = userService.getUser(passwordResetTokenDTO.getEmail());
        UserDTO updatedUserDTO =
                UserDTO.builder(userDTO).password(encoder.encode(request.password())).build();
        userService.save(updatedUserDTO);
        passwordResetTokenService.update(PasswordResetTokenDTO.builder(passwordResetTokenDTO)
                .confirmDate(LocalDateTime.now()).build());
        return new MessageResponse("Password changed successfully.");
    }
}
