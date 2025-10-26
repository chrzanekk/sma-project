package pl.com.chrzanowski.sma.auth.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.chrzanowski.sma.auth.dto.request.UserEditPasswordChangeRequest;
import pl.com.chrzanowski.sma.auth.dto.request.UserEditRoleUpdateRequest;
import pl.com.chrzanowski.sma.auth.dto.request.UserUpdateRequest;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.service.RoleService;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.service.UserService;

import java.util.HashSet;
import java.util.Set;


@RestController
@RequestMapping(ApiPath.ACCOUNT)
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);
    private final UserService userService;
    private final RoleService roleService;

    public AccountController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/get")
    public ResponseEntity<UserInfoResponse> getAccount() {
        log.debug("REST: Get account information");
        UserInfoResponse userInfoResponse = userService.getUserWithAuthorities();
        return ResponseEntity.ok(userInfoResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateAccount(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        log.debug("REST: Update account for user: {}", userUpdateRequest.getLogin());
        Set<RoleDTO> roleDTOList = roleService.findAllByListOfNames(userUpdateRequest.getRoles());
        UserDTO userDTO = UserDTO.builder()
                .id(userUpdateRequest.getId())
                .login(userUpdateRequest.getLogin())
                .email(userUpdateRequest.getEmail())
                .firstName(userUpdateRequest.getFirstName())
                .lastName(userUpdateRequest.getLastName())
                .position(userUpdateRequest.getPosition())
                .roles(roleDTOList)
                .locked(userUpdateRequest.getLocked())
                .enabled(userUpdateRequest.getEnabled()).build();
        userService.update(userDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UserEditPasswordChangeRequest userEditPasswordChangeRequest) {
        log.debug("REST: Change password for user id:{}", userEditPasswordChangeRequest.userId());
        userService.updateUserPassword(userEditPasswordChangeRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateRoles(@RequestBody UserEditRoleUpdateRequest userEditRoleUpdateRequest) {
        log.debug("REST: Update roles for user id:{}", userEditRoleUpdateRequest.userId());
        userService.updateUserRoles(userEditRoleUpdateRequest.userId(), new HashSet<>(userEditRoleUpdateRequest.roles()));
        return ResponseEntity.noContent().build();
    }
}
