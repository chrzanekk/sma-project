package pl.com.chrzanowski.sma.common.security.resource.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
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

    @Override
    public List<ResourceDTO> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

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

        return toDTO(saved);
    }

    @Override
    public void reloadSecurityConfiguration() {
        // Trigger reinitialization of SecurityFilterChain
        // W Spring Boot 3.x może wymagać restartu lub custom reload mechanism
        log.warn("Security reload requested - may require application restart");
        // Możesz zaimplementować custom mechanism z RefreshScope
    }

}
