
package pl.com.chrzanowski.sma.common.security.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.common.security.enums.ResourceKey;
import pl.com.chrzanowski.sma.common.security.resource.model.Resource;
import pl.com.chrzanowski.sma.common.security.resource.repository.ResourceRepository;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.repository.RoleRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ResourceInitializer {

    private final ResourceRepository resourceRepository;
    private final RoleRepository roleRepository;

    public ResourceInitializer(ResourceRepository resourceRepository, RoleRepository roleRepository) {
        this.resourceRepository = resourceRepository;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    @Transactional
    public void initializeResources() {
        log.info("Starting resource synchronization...");

        // Get ROLE_ADMIN (default role for new resources)
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found - cannot initialize resources"));

        // Get all resource keys from enum
        Set<ResourceKey> enumResourceKeys = Arrays.stream(ResourceKey.values())
                .collect(Collectors.toSet());

        // Get existing resources from database
        List<Resource> existingResources = resourceRepository.findAll();
        Set<ResourceKey> existingResourceKeys = existingResources.stream()
                .map(Resource::getResourceKey)
                .collect(Collectors.toSet());

        // ✅ 1. Add new resources (present in enum but not in database)
        Set<ResourceKey> newResourceKeys = enumResourceKeys.stream()
                .filter(key -> !existingResourceKeys.contains(key))
                .collect(Collectors.toSet());

        if (!newResourceKeys.isEmpty()) {
            log.info("Found {} new resources to add", newResourceKeys.size());
            newResourceKeys.forEach(resourceKey -> {
                Resource newResource = Resource.builder()
                        .resourceKey(resourceKey)
                        .endpointPattern(resourceKey.getEndpointPattern())
                        .httpMethod(resourceKey.getHttpMethod())
                        .description(resourceKey.getDescription())
                        .isActive(true)
                        .allowedRoles(Set.of(adminRole)) // ✅ Automatically assign ROLE_ADMIN to new resources
                        .build();

                resourceRepository.save(newResource);
                log.info("Added new resource: {} with ROLE_ADMIN", resourceKey);
            });
        }

        // ✅ 2. Remove obsolete resources (present in database but not in enum)
        List<Resource> obsoleteResources = existingResources.stream()
                .filter(resource -> !enumResourceKeys.contains(resource.getResourceKey()))
                .toList();

        if (!obsoleteResources.isEmpty()) {
            log.info("Found {} obsolete resources to remove", obsoleteResources.size());
            obsoleteResources.forEach(resource -> {
                resourceRepository.delete(resource);
                log.info("Removed obsolete resource: {}", resource.getResourceKey());
            });
        }

        // ✅ 3. Update existing resources (sync description, endpoint pattern, etc.)
        existingResources.stream()
                .filter(resource -> enumResourceKeys.contains(resource.getResourceKey()))
                .forEach(resource -> {
                    ResourceKey enumKey = resource.getResourceKey();
                    boolean updated = false;

                    // Update description if changed
                    if (!enumKey.getDescription().equals(resource.getDescription())) {
                        resource.setDescription(enumKey.getDescription());
                        updated = true;
                    }

                    // Update endpoint pattern if changed
                    if (!enumKey.getEndpointPattern().equals(resource.getEndpointPattern())) {
                        resource.setEndpointPattern(enumKey.getEndpointPattern());
                        updated = true;
                    }

                    // Update HTTP method if changed
                    if (enumKey.getHttpMethod() != null && !enumKey.getHttpMethod().equals(resource.getHttpMethod())) {
                        resource.setHttpMethod(enumKey.getHttpMethod());
                        updated = true;
                    }

                    if (updated) {
                        resourceRepository.save(resource);
                        log.info("Updated resource: {}", resource.getResourceKey());
                    }
                });

        // ✅ 4. Assign ROLE_ADMIN to resources without any roles
        List<Resource> resourcesWithoutRoles = resourceRepository.findAll().stream()
                .filter(r -> r.getAllowedRoles().isEmpty())
                .toList();

        if (!resourcesWithoutRoles.isEmpty()) {
            log.warn("Found {} resources without roles, assigning ROLE_ADMIN", resourcesWithoutRoles.size());
            resourcesWithoutRoles.forEach(resource -> {
                resource.getAllowedRoles().add(adminRole);
                resourceRepository.save(resource);
                log.info("Assigned ROLE_ADMIN to resource: {}", resource.getResourceKey());
            });
        }

        log.info("Resource synchronization completed");
    }
}
