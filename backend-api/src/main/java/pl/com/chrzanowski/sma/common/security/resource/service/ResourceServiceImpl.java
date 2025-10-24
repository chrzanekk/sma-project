package pl.com.chrzanowski.sma.common.security.resource.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.security.resource.dto.ResourceDTO;
import pl.com.chrzanowski.sma.common.security.resource.model.Resource;
import pl.com.chrzanowski.sma.common.security.resource.repository.ResourceRepository;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.repository.RoleRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;
    private final RoleRepository roleRepository;
    private final ApplicationContext applicationContext;

    public ResourceServiceImpl(ResourceRepository resourceRepository, RoleRepository roleRepository, ApplicationContext applicationContext) {
        this.resourceRepository = resourceRepository;
        this.roleRepository = roleRepository;
        this.applicationContext = applicationContext;
    }

    /**
     * Get all resources - for admin panel
     */
    @Override
    public List<ResourceDTO> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    /**
     * Get resources for current user - returns only resources user has access to
     */
    @Override
    public List<ResourceDTO> getResourcesForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Service: No authenticated user found");
            return List.of();
        }

        // Get user's roles/authorities
        Set<String> userAuthorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        log.debug("Service: Get resources for user with authorities: {}", userAuthorities);

        // Get all resources
        List<Resource> allResources = resourceRepository.findAll();

        // Filter resources based on user's authorities
        return allResources.stream()
                .filter(resource -> {
                    // Public resources are always included
                    if (resource.getResourceKey().isPublic()) {
                        return true;
                    }

                    // Check if user has any of the required roles
                    Set<String> resourceRoleNames = resource.getAllowedRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toSet());

                    // User can access if they have at least one of the required roles
                    return userAuthorities.stream()
                            .anyMatch(resourceRoleNames::contains);
                })
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update resource roles - admin only
     */
    @Transactional
    @Override
    public ResourceDTO updateResourceRoles(Long resourceId, List<String> roleNames) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found: " + resourceId));

        Set<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        resource.setAllowedRoles(roles);
        Resource saved = resourceRepository.save(resource);

        log.info("Updated roles for resource {}: {}", resourceId, roleNames);

        // Trigger security reload if needed
//        reloadSecurityConfiguration();

        return toDTO(saved);
    }

    /**
     * Reload security configuration
     */
    @Override
    public void reloadSecurityConfiguration() {
        // In Spring Boot 3.x, SecurityFilterChain is immutable after creation
        // This would require application restart or using @RefreshScope
        log.warn("Security configuration updated - changes will take effect on next application restart");

        // Optional: You can trigger a custom event or use @RefreshScope
        // applicationContext.publishEvent(new SecurityConfigurationChangedEvent(this));
    }

}
