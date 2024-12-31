package pl.com.chrzanowski.sma.role.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.chrzanowski.sma.common.util.controller.PaginationUtil;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.service.RoleQueryService;
import pl.com.chrzanowski.sma.role.service.RoleService;
import pl.com.chrzanowski.sma.role.service.filter.RoleFilter;

import java.util.List;

@RestController
@RequestMapping(path = "/api/roles")
public class RoleController {

    private final Logger log = LoggerFactory.getLogger(RoleController.class);

    private final RoleService roleService;
    private final RoleQueryService roleQueryService;

    public RoleController(RoleService roleService, RoleQueryService roleQueryService) {
        this.roleService = roleService;
        this.roleQueryService = roleQueryService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoleDTO>> getRoles() {
        log.debug("REST request to get roles");
        return ResponseEntity.ok().body(roleService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<RoleDTO>> getRolesPage(RoleFilter roleFilter, Pageable pageable) {
        log.debug("REST request to get roles by page: {}", pageable);
        Page<RoleDTO> result = roleQueryService.findByFilter(roleFilter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequestUri(), result);
        return ResponseEntity.ok().headers(headers).body(result);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<RoleDTO>> getRolesByFilter(RoleFilter roleFilter) {
        log.debug("REST request to get roles by filter: {}", roleFilter);
        List<RoleDTO> result = roleQueryService.findByFilter(roleFilter);
        return ResponseEntity.ok().body(result);
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
