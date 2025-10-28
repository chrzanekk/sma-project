package pl.com.chrzanowski.sma.common.security.resource.service;

import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.security.resource.dto.ResourceDTO;
import pl.com.chrzanowski.sma.common.security.resource.model.Resource;
import pl.com.chrzanowski.sma.role.model.Role;

import java.util.List;
import java.util.stream.Collectors;

public interface ResourceService {
    /**
     * Get all resources - admin only (for resource management panel)
     */
    List<ResourceDTO> getAllResources();

    /**
     * Get resources filtered by current user's permissions
     */
    List<ResourceDTO> getResourcesForCurrentUser();

    /**
     * Update resource roles - admin only
     */
    ResourceDTO updateResourceRoles(Long resourceId, List<String> roleNames);


    /**
     * Reload security configuration
     */
    void reloadSecurityConfiguration();

    default ResourceDTO toDTO(Resource resource) {
        return ResourceDTO.builder()
                .id(resource.getId())
                .resourceKey(resource.getResourceKey().name())
                .displayName(resource.getResourceKey().getDisplayName())
                .endpointPattern(resource.getEndpointPattern())
                .description(resource.getDescription())
                .httpMethod(resource.getHttpMethod())
                .isActive(resource.getIsActive())
                .isPublic(resource.getResourceKey().isPublic())
                .allowedRoles(resource.getAllowedRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()))
                .build();
    }
}
