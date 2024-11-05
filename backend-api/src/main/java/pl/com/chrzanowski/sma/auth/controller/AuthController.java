package pl.com.chrzanowski.sma.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.com.chrzanowski.sma.auth.dto.request.LoginRequest;
import pl.com.chrzanowski.sma.auth.dto.request.NewPasswordPutRequest;
import pl.com.chrzanowski.sma.auth.dto.request.PasswordResetRequest;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.response.JWTToken;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.auth.service.PasswordResetService;
import pl.com.chrzanowski.sma.common.enumeration.TokenType;
import pl.com.chrzanowski.sma.common.exception.EmailAlreadyExistsException;
import pl.com.chrzanowski.sma.common.exception.EmailNotFoundException;
import pl.com.chrzanowski.sma.common.exception.PasswordNotMatchException;
import pl.com.chrzanowski.sma.common.exception.UsernameAlreadyExistsException;
import pl.com.chrzanowski.sma.common.security.jwt.AuthTokenFilter;
import pl.com.chrzanowski.sma.common.security.jwt.JwtUtils;
import pl.com.chrzanowski.sma.common.util.TokenUtil;
import pl.com.chrzanowski.sma.email.service.SendEmailService;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.service.UserService;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;
import pl.com.chrzanowski.sma.usertoken.service.UserTokenService;

import java.util.Locale;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

    @Value("${jwt.tokenValidityTimeInMinutes}")
    private Long tokenValidityTimeInMinutes;

    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    private final PasswordResetService passwordResetService;
    private final SendEmailService sendEmailService;
    private final UserService userService;
    private final UserTokenService userTokenService;


    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordResetService passwordResetService, SendEmailService sendEmailService, UserService userService, UserTokenService userTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordResetService = passwordResetService;
        this.sendEmailService = sendEmailService;
        this.userService = userService;
        this.userTokenService = userTokenService;
    }


    @PostMapping("/login")
    public ResponseEntity<JWTToken> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("REST request to login user {}", loginRequest);
        LoginRequest updatedRequest =
                loginRequest.toBuilder().username(loginRequest.getUsername().toLowerCase()).build();
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

        MessageResponse response = sendEmailService.sendAfterRegistration(userTokenDTO, new Locale("pl"));
        return ResponseEntity.ok().header("Confirmation-Token", userTokenDTO.getToken()).body(response);
    }

    @GetMapping("/confirm")
    public ResponseEntity<MessageResponse> confirmRegistration(@RequestParam("token") String token) {
        log.debug("REST request to confirm user registration. Token: {}", token);
        UserTokenDTO userTokenDTO = userTokenService.getTokenData(token);

        TokenUtil.validateTokenTime(userTokenDTO.getCreateDate(), tokenValidityTimeInMinutes);
        sendEmailService.sendAfterEmailConfirmation(userTokenDTO, new Locale("pl"));
        MessageResponse response = new MessageResponse(userService.confirm(token));
        return ResponseEntity.ok().body(response);
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
        MessageResponse response = sendEmailService.sendPasswordResetMail(userTokenDTO, new Locale("pl"));
        return ResponseEntity.ok().header("Reset-Token", userTokenDTO.getToken()).body(response);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> newPasswordPut(@RequestBody NewPasswordPutRequest request) {
        log.debug("REST request to set new password by token: {}", request.token());
        validatePasswordMatch(request);
        UserTokenDTO userTokenDTO = userTokenService.getTokenData(request.token());

        TokenUtil.validateTokenTime(userTokenDTO.getCreateDate(), tokenValidityTimeInMinutes);
        MessageResponse response = passwordResetService.saveNewPassword(userTokenDTO, request);
        sendEmailService.sendAfterPasswordChange(userTokenDTO, new Locale("pl"));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<Boolean> isAuthenticated() {
        log.debug("REST request to check if the current user is authenticated");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        return ResponseEntity.ok(isAuthenticated);
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
}
