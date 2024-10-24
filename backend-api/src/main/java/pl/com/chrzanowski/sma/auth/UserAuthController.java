package pl.com.chrzanowski.sma.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.com.chrzanowski.sma.auth.request.LoginRequest;
import pl.com.chrzanowski.sma.auth.request.NewPasswordPutRequest;
import pl.com.chrzanowski.sma.auth.request.PasswordResetRequest;
import pl.com.chrzanowski.sma.auth.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.response.MessageResponse;
import pl.com.chrzanowski.sma.auth.usertokens.UserTokenDTO;
import pl.com.chrzanowski.sma.auth.usertokens.UserTokenService;
import pl.com.chrzanowski.sma.email.SentEmailService;
import pl.com.chrzanowski.sma.common.enumeration.TokenType;
import pl.com.chrzanowski.sma.common.exception.EmailAlreadyExistsException;
import pl.com.chrzanowski.sma.common.exception.EmailNotFoundException;
import pl.com.chrzanowski.sma.common.exception.PasswordNotMatchException;
import pl.com.chrzanowski.sma.common.exception.UsernameAlreadyExistsException;
import pl.com.chrzanowski.sma.common.security.jwt.AuthTokenFilter;
import pl.com.chrzanowski.sma.common.security.jwt.JwtUtils;
import pl.com.chrzanowski.sma.user.UserDTO;
import pl.com.chrzanowski.sma.user.UserService;
import pl.com.chrzanowski.sma.common.util.TokenUtil;

import java.util.Locale;

@RestController
@RequestMapping(path = "/api/auth")
public class UserAuthController {

    @Value("${jwt.tokenValidityTimeInMinutes}")
    private Long tokenValidityTimeInMinutes;

    private final Logger log = LoggerFactory.getLogger(UserAuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    private final PasswordResetService passwordResetService;
    private final SentEmailService sentEmailService;
    private final UserService userService;
    private final UserTokenService userTokenService;


    public UserAuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordResetService passwordResetService, SentEmailService sentEmailService, UserService userService, UserTokenService userTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordResetService = passwordResetService;
        this.sentEmailService = sentEmailService;
        this.userService = userService;
        this.userTokenService = userTokenService;
    }


    @PostMapping("/login")
    public ResponseEntity<JWTToken> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("REST request to login user {}", loginRequest);
        LoginRequest updatedRequest =
                LoginRequest.builder(loginRequest).username(loginRequest.getUsername().toLowerCase()).build();
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(updatedRequest.getUsername(), updatedRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthTokenFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), headers, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok().build();
    }


    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        log.debug("REST request to register new user {}", registerRequest);
        RegisterRequest updatedRequest =
                RegisterRequest.builder(registerRequest).username(registerRequest.getUsername().toLowerCase()).build();
        if (isUsernameTaken(updatedRequest.getUsername())) {
            throw new UsernameAlreadyExistsException("Error. Username is already in use.");
        }

        if (isEmailTaken(updatedRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Error. Email is already in use!");
        }

        UserDTO savedUser = userService.register(updatedRequest);
        String generatedToken = userTokenService.generateToken();
        UserTokenDTO userTokenDTO = userTokenService.saveToken(generatedToken, savedUser, TokenType.CONFIRMATION_TOKEN);

        MessageResponse response = sentEmailService.sendAfterRegistration(userTokenDTO, new Locale("pl"));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/confirm")
    public String confirmRegistration(@RequestParam("token") String token) {
        log.debug("REST request to confirm user registration. Token: {}", token);
        UserTokenDTO userTokenDTO = userTokenService.getTokenData(token);

        TokenUtil.validateTokenTime(userTokenDTO.getCreateDate(), tokenValidityTimeInMinutes);
        sentEmailService.sendAfterEmailConfirmation(userTokenDTO, new Locale("pl"));
        return userService.confirm(token);
    }

    @PutMapping("/request-password-reset")
    @Transactional
    public ResponseEntity<?> passwordReset(@Valid @NotNull @RequestBody PasswordResetRequest passwordResetRequest) {
        log.debug("REST request to set new password for user: {}", passwordResetRequest.getEmail());

        if (!isEmailTaken(passwordResetRequest.getEmail())) {
            throw new EmailNotFoundException("Email not found");
        }
        UserDTO userDTO = userService.getUser(passwordResetRequest.getEmail());
        String token = userTokenService.generateToken();
        UserTokenDTO userTokenDTO = userTokenService.saveToken(token, userDTO, TokenType.PASSWORD_RESET_TOKEN);
        MessageResponse response = sentEmailService.sendPasswordResetMail(userTokenDTO, new Locale("pl"));
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> newPasswordPut(@RequestBody NewPasswordPutRequest request) {
        log.debug("REST request to set new password by token: {}", request.token());
        validatePasswordMatch(request);
        UserTokenDTO userTokenDTO = userTokenService.getTokenData(request.token());

        TokenUtil.validateTokenTime(userTokenDTO.getCreateDate(), tokenValidityTimeInMinutes);
        MessageResponse response = passwordResetService.saveNewPassword(userTokenDTO, request);
        sentEmailService.sendAfterPasswordChange(userTokenDTO, new Locale("pl"));
        return ResponseEntity.ok().body(response);
    }

    private boolean isEmailTaken(String email) {
        return Boolean.TRUE.equals(userService.isEmailExists(email));
    }

    private boolean isUsernameTaken(String userName) {
        return Boolean.TRUE.equals(userService.isUserExists(userName));
    }

    private void validatePasswordMatch(NewPasswordPutRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new PasswordNotMatchException("Password not match");
        }
    }

    static class JWTToken {

        private String tokenValue;

        JWTToken(String tokenValue) {
            this.tokenValue = tokenValue;
        }

        @JsonProperty("id_token")
        String getTokenValue() {
            return tokenValue;
        }

        void setTokenValue(String tokenValue) {
            this.tokenValue = tokenValue;
        }
    }

}
