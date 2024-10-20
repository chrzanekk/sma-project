package pl.com.chrzanowski.scma.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.scma.payload.request.NewPasswordPutRequest;
import pl.com.chrzanowski.scma.payload.response.MessageResponse;
import pl.com.chrzanowski.scma.service.PasswordResetService;
import pl.com.chrzanowski.scma.service.PasswordResetTokenService;
import pl.com.chrzanowski.scma.service.UserService;
import pl.com.chrzanowski.scma.service.dto.PasswordResetTokenDTO;
import pl.com.chrzanowski.scma.service.dto.UserDTO;

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
