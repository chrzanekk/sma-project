package pl.com.chrzanowski.sma.role.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.service.RoleService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api/roles")
public class RoleController {

    private final Logger log = LoggerFactory.getLogger(RoleController.class);

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoleDTO>> getRoles() {
        log.debug("REST request to get roles");
        return ResponseEntity.ok().body(roleService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<RoleDTO> saveRole(@RequestBody RoleDTO roleDTO) {
        log.debug("REST request to save Role : {}", roleDTO);
        return ResponseEntity.ok().body(roleService.saveRole(roleDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteRole(@PathVariable Long id) {
        log.debug("REST request to delete Role : {}", id);
        boolean result = roleService.deleteById(id);
        if (result) {
            return ResponseEntity.ok().body(true);
        }
        return ResponseEntity.noContent().build();
    }
}
