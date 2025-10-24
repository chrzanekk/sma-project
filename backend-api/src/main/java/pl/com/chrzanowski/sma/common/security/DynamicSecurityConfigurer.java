package pl.com.chrzanowski.sma.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import pl.com.chrzanowski.sma.common.security.resource.model.Resource;
import pl.com.chrzanowski.sma.common.security.resource.repository.ResourceRepository;
import pl.com.chrzanowski.sma.role.model.Role;

import java.util.List;

@Configuration
@Slf4j
public class DynamicSecurityConfigurer {
    private final ResourceRepository resourceRepository;

    public DynamicSecurityConfigurer(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public void configureAuthorization(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth
    ) {
        List<Resource> resources = resourceRepository.findAllActiveWithRoles();

        log.info("Configuring security for {} resources", resources.size());

        resources.forEach(resource -> {
            String[] roleNames = resource.getAllowedRoles().stream()
                    .map(Role::getName)
                    .toArray(String[]::new);

            if (resource.getResourceKey().isPublic() || roleNames.length == 0) {
                // Public endpoint
                if (resource.getHttpMethod() != null) {
                    auth.requestMatchers(
                            HttpMethod.valueOf(resource.getHttpMethod()),
                            resource.getEndpointPattern()
                    ).permitAll();
                } else {
                    auth.requestMatchers(resource.getEndpointPattern()).permitAll();
                }
                log.debug("Configured PUBLIC access for: {}", resource.getEndpointPattern());
            } else {
                // Protected endpoint
                if (resource.getHttpMethod() != null) {
                    auth.requestMatchers(
                            HttpMethod.valueOf(resource.getHttpMethod()),
                            resource.getEndpointPattern()
                    ).hasAnyAuthority(roleNames);
                } else {
                    auth.requestMatchers(resource.getEndpointPattern())
                            .hasAnyAuthority(roleNames);
                }
                log.debug("Configured PROTECTED access for: {} with roles: {}",
                        resource.getEndpointPattern(), String.join(", ", roleNames));
            }
        });

        // Deny all other requests
        auth.anyRequest().denyAll();
    }
}
