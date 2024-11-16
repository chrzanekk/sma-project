package pl.com.chrzanowski.sma.auth.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.dto.UserPasswordChangeRequest;
import pl.com.chrzanowski.sma.user.service.UserService;


@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);
    private final UserService userService;


    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public ResponseEntity<UserInfoResponse> getAccount() {
        log.debug("REST: Get account information");
        UserInfoResponse userInfoResponse = userService.getUserWithAuthorities();
        return ResponseEntity.ok(userInfoResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateAccount(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST: Update account {}", userDTO);
        userService.update(userDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UserPasswordChangeRequest userPasswordChangeRequest) {
        log.debug("REST: Change password for user id:{}", userPasswordChangeRequest.userId());
        userService.updateUserPassword(userPasswordChangeRequest);
        return ResponseEntity.noContent().build();
    }
}
