// security/authorization/DynamicResourceAuthorizationManager.java
package pl.com.chrzanowski.sma.common.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.common.security.resource.model.Resource;
import pl.com.chrzanowski.sma.common.security.resource.repository.ResourceRepository;
import pl.com.chrzanowski.sma.role.model.Role;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DynamicResourceAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final ResourceRepository resourceRepository;

    // ✅ HTTP methods that are considered READ-ONLY
    private static final Set<String> READ_ONLY_METHODS = Set.of("GET", "HEAD", "OPTIONS");


    public DynamicResourceAuthorizationManager(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        Authentication authentication = authenticationSupplier.get();

        // ✅ Jeśli nie ma autentykacji, odmowa
        if (authentication == null || !authentication.isAuthenticated()) {
            log.debug("🚫 No authentication found");
            return new AuthorizationDecision(false);
        }

        String requestPath = context.getRequest().getRequestURI();
        String httpMethod = context.getRequest().getMethod();

        log.debug("🔍 Checking authorization for: {} {}", httpMethod, requestPath);

        List<Resource> allResources = resourceRepository.findAllActiveWithRoles();
        log.info("📋 Found {} total resources in database", allResources.size());

        Set<Resource> matchingResources = allResources.stream()
                .filter(resource -> matchesPattern(requestPath, resource.getEndpointPattern(), httpMethod, resource.getHttpMethod()))
                .collect(Collectors.toSet());

        log.info("🎯 Found {} matching resources", matchingResources.size());

        if (matchingResources.isEmpty()) {
            log.warn("⚠️ No matching resource found for: {} {}", httpMethod, requestPath);
            return new AuthorizationDecision(false);
        }

        // ✅ Pobierz role użytkownika
        Set<String> userAuthorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        log.debug("👤 User authorities: {}", userAuthorities);


        for (Resource resource : matchingResources) {
            log.info("🔐 Checking resource: {} ({})", resource.getResourceKey(), resource.getEndpointPattern());
            if (resource.getResourceKey().isPublic()) {
                log.debug("✅ Public resource: {}", resource.getResourceKey());
                return new AuthorizationDecision(true);
            }

            Set<String> requiredRoles = resource.getAllowedRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());

            log.debug("🔐 Resource {} requires roles: {}", resource.getResourceKey(), requiredRoles);

            if (userAuthorities.stream().anyMatch(requiredRoles::contains)) {
                log.debug("✅ Access granted for resource: {}", resource.getResourceKey());
                return new AuthorizationDecision(true);
            }

            // ✅ 2. Dependency access - check if user has access via parent resource
            // Only for READ-ONLY operations (GET, HEAD, OPTIONS)
            if (READ_ONLY_METHODS.contains(httpMethod.toUpperCase())) {
                if (hasAccessViaDependency(resource, allResources, userAuthorities)) {
                    log.info("✅ Access GRANTED (via dependency - READ-ONLY)");
                    return new AuthorizationDecision(true);
                }
            } else {
                log.debug("   ⏩ Skipping dependency check for non-READ method: {}", httpMethod);
            }
        }

        log.debug("🚫 Access denied - no matching roles");
        return new AuthorizationDecision(false);
    }

    /**
     * Check if user has access to this resource via dependency from parent resource
     * Only grants READ-ONLY access (GET, HEAD, OPTIONS)
     */
    private boolean hasAccessViaDependency(Resource targetResource, List<Resource> allResources,
                                           Set<String> userAuthorities) {
        String targetResourceKey = targetResource.getResourceKey().name();

        log.debug("   🔗 Checking dependency access for: {}", targetResourceKey);

        // Find resources that have this resource as dependency
        boolean hasAccess = allResources.stream()
                .filter(parentResource -> {
                    Set<String> dependencies = parentResource.getResourceKey().getDependencies();
                    return dependencies.contains(targetResourceKey);
                })
                .anyMatch(parentResource -> {
                    Set<String> parentRequiredRoles = parentResource.getAllowedRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toSet());

                    boolean hasParentAccess = userAuthorities.stream().anyMatch(parentRequiredRoles::contains);

                    if (hasParentAccess) {
                        log.info("   ✅ User has access to parent resource: {} → grants READ access to {}",
                                parentResource.getResourceKey(), targetResourceKey);
                    }

                    return hasParentAccess;
                });

        if (!hasAccess) {
            log.debug("   ❌ No parent resource with access found");
        }

        return hasAccess;
    }


    /**
     * Check if request path matches resource pattern
     * Supports wildcards: /** (multi-level) and /* (single-level)
     */
    private boolean matchesPattern(String requestPath, String pattern, String requestMethod, String resourceMethod) {
        // Sprawdź metodę HTTP
        if (resourceMethod != null && !resourceMethod.equalsIgnoreCase(requestMethod)) {
            return false;
        }

        // Exact match
        if (requestPath.equals(pattern)) {
            return true;
        }

        // /** wildcard - matches everything under the path
        // Example: /api/users/** matches /api/users/1, /api/users/1/edit, etc.
        if (pattern.endsWith("/**")) {
            String basePattern = pattern.substring(0, pattern.length() - 3); // Remove /**
            return requestPath.startsWith(basePattern);
        }

        // /* wildcard - matches single path segment
        // Example: /api/users/* matches /api/users/1, but NOT /api/users/1/edit
        if (pattern.endsWith("/*")) {
            String basePattern = pattern.substring(0, pattern.length() - 2); // Remove /*
            if (!requestPath.startsWith(basePattern)) {
                return false;
            }
            String remaining = requestPath.substring(basePattern.length());
            // Sprawdź czy po basePattern jest tylko jeden segment (bez kolejnych /)
            return !remaining.isEmpty() && !remaining.substring(1).contains("/");
        }

        return false;
    }

}
