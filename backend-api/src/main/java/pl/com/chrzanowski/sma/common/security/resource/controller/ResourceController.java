package pl.com.chrzanowski.sma.common.security.resource.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
import pl.com.chrzanowski.sma.common.security.resource.dto.ResourceDTO;
import pl.com.chrzanowski.sma.common.security.resource.dto.ResourceUpdateRequest;
import pl.com.chrzanowski.sma.common.security.resource.service.ResourceService;

import java.util.List;

@RestController
@RequestMapping(ApiPath.RESOURCE)
@Slf4j
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * Get resources for current user
     * Available for all authenticated users
     * Returns only resources the user has access to
     */
    @GetMapping
    public ResponseEntity<List<ResourceDTO>> getAllResources(Authentication authentication) {
        log.info("REST: Get resources for user: {}", authentication.getName());
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    /**
     * Get ALL resources - only for admins (for resource management panel)
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<ResourceDTO>> getAllResourcesAdmin() {
        log.info("REST: Get all resources (admin)");
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResourceDTO> updateResourceRoles(
            @PathVariable Long id,
            @RequestBody ResourceUpdateRequest request
    ) {
        log.info("REST: Update roles for resource id: {}", id);
        return ResponseEntity.ok(resourceService.updateResourceRoles(id, request.getRoleNames()));
    }

    @PostMapping("/reload")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> reloadSecurity() {
        log.info("REST: Reload security configuration");
        resourceService.reloadSecurityConfiguration();
        return ResponseEntity.ok().build();
    }
}
