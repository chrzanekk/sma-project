package pl.com.chrzanowski.sma.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.chrzanowski.sma.common.util.controller.PaginationUtil;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.service.filter.UserFilter;
import pl.com.chrzanowski.sma.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<UserDTO>> getUsersByFilter(UserFilter userFilter) {
        log.debug("REST request to get all users by filter: {}", userFilter);
        List<UserDTO> result = userService.findByFilter(userFilter);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.debug("REST request to get all users.");
        List<UserDTO> result = userService.findAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(path = "/page")
    public ResponseEntity<List<UserDTO>> getUsersByFilterAndPage(UserFilter userFilter, Pageable pageable) {
        log.debug("REST request to get all users by filter and page: {},{}", userFilter, pageable);
        Page<UserDTO> result = userService.findByFilterAndPage(userFilter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(), result);
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        log.debug("REST request to get user by id: {}", id);
        UserDTO userDTO = userService.findById(id);
        return ResponseEntity.ok().body(userDTO);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        log.debug("REST request to delete user by id: {}", id);
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

}
