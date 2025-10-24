package pl.com.chrzanowski.sma.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;
import pl.com.chrzanowski.sma.common.security.enums.ResourceKey;
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
        // ✅ First: Configure specific endpoints that should be accessible

        // Public authentication endpoints
        auth.requestMatchers(
                ApiPath.AUTH + "/login",
                ApiPath.AUTH + "/register",
                ApiPath.AUTH + "/confirm",
                ApiPath.AUTH + "/request-password-reset",
                ApiPath.AUTH + "/reset-password"
        ).permitAll();

        // ✅ Resources endpoint - accessible for authenticated users
        auth.requestMatchers(HttpMethod.GET, ApiPath.RESOURCE).authenticated();

        // ✅ Admin-only resource management endpoints
        auth.requestMatchers(ResourceKey.RESOURCE_MANAGEMENT.getEndpointPattern()).hasAuthority("ROLE_ADMIN");

        // ✅ Load dynamic resources from database
        List<Resource> resources = resourceRepository.findAllActiveWithRoles();
        log.info("Configuring security for {} resources from database", resources.size());


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
