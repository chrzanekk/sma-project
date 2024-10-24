package pl.com.chrzanowski.sma.auth.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
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
        return userService.getUserWithAuthorities();
    }

    @PostMapping("/save")
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        //todo implement save updated user info
    }




}
