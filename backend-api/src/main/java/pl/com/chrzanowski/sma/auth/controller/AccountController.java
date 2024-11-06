package pl.com.chrzanowski.sma.auth.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.user.dto.UserPasswordChangeRequest;
import pl.com.chrzanowski.sma.user.service.UserService;
import pl.com.chrzanowski.sma.user.dto.UserDTO;


@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);
    private final UserService userService;


    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public UserInfoResponse getAccount() {
        log.info("REST: Get account information");
        return userService.getUserWithAuthorities();
    }

    @PutMapping("/update")
    public void updatAccount(@Valid @RequestBody UserDTO userDTO) {
        log.info("REST: Update account {}", userDTO);
        userService.update(userDTO);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@Valid @RequestBody UserPasswordChangeRequest userPasswordChangeRequest) {
        log.info("REST: Change password for user id:{}", userPasswordChangeRequest.userId());
        userService.updateUserPassword(userPasswordChangeRequest);
        return ResponseEntity.ok(true);
    }
}
