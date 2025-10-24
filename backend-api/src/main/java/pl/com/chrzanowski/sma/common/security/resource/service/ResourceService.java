package pl.com.chrzanowski.sma.common.security.resource.service;

import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.security.resource.dto.ResourceDTO;
import pl.com.chrzanowski.sma.common.security.resource.model.Resource;
import pl.com.chrzanowski.sma.role.model.Role;

import java.util.List;
import java.util.stream.Collectors;

public interface ResourceService {
    List<ResourceDTO> getAllResources();

    @Transactional
    ResourceDTO updateResourceRoles(Long resourceId, List<String> roleNames);

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
