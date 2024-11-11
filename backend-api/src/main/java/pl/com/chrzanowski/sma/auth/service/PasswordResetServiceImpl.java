package pl.com.chrzanowski.sma.auth.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.auth.dto.request.NewPasswordPutRequest;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.service.UserService;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;
import pl.com.chrzanowski.sma.usertoken.service.UserTokenService;

import java.time.LocalDateTime;

@Service
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {

    private final Logger log = LoggerFactory.getLogger(PasswordResetServiceImpl.class);

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final UserTokenService userTokenService;

    public PasswordResetServiceImpl(UserService userService,
                                    PasswordEncoder encoder, UserTokenService userTokenService) {
        this.userService = userService;
        this.encoder = encoder;
        this.userTokenService = userTokenService;
    }

    @Override
    public MessageResponse saveNewPassword(UserTokenDTO userTokenDTO, NewPasswordPutRequest request) {
        log.debug("Request to save new password.");
        UserDTO userDTO = userService.getUser(userTokenDTO.getEmail());
        UserDTO updatedUserDTO =
                userDTO.toBuilder().password(encoder.encode(request.password())).build();
        userService.save(updatedUserDTO);
        userTokenService.updateToken(userTokenDTO.toBuilder()
                .useDate(LocalDateTime.now()).build());
        return new MessageResponse("Password changed successfully");
    }
}
