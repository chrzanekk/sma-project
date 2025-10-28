// security/DynamicSecurityConfigurer.java
package pl.com.chrzanowski.sma.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import pl.com.chrzanowski.sma.common.security.config.DynamicResourceAuthorizationManager;
import pl.com.chrzanowski.sma.common.security.enums.ApiPath;

@Configuration
@Slf4j
public class DynamicSecurityConfigurer {

    private final DynamicResourceAuthorizationManager dynamicAuthorizationManager;

    public DynamicSecurityConfigurer(DynamicResourceAuthorizationManager dynamicAuthorizationManager) {
        this.dynamicAuthorizationManager = dynamicAuthorizationManager;
    }

    public void configureAuthorization(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth
    ) {
        log.info("🔐 Configuring dynamic security with runtime authorization checks");

        // ✅ 1. Public authentication endpoints
        auth.requestMatchers(
                ApiPath.AUTH + "/login",
                ApiPath.AUTH + "/register",
                ApiPath.AUTH + "/confirm",
                ApiPath.AUTH + "/request-password-reset",
                ApiPath.AUTH + "/reset-password"
        ).permitAll();

        // ✅ 2. Account endpoints - accessible for ALL authenticated users
        auth.requestMatchers(ApiPath.ACCOUNT + "/**").authenticated();

        // ✅ 3. Resources endpoint - accessible for authenticated users (to get their permissions)
        auth.requestMatchers(HttpMethod.GET, ApiPath.RESOURCE).authenticated();

        // ✅ 4. Admin-only resource management endpoints (hardcoded to prevent lockout)
        auth.requestMatchers(ApiPath.RESOURCE + "/all",
                ApiPath.RESOURCE + "/*/roles",
                ApiPath.RESOURCE + "/reload"
        ).hasAuthority("ROLE_ADMIN");

        // ✅ 5. Deny other /api/resources/** endpoints (if any)
        auth.requestMatchers(ApiPath.RESOURCE + "/**").denyAll();

        // ✅ 6. **CRITICAL**: All other API endpoints use DYNAMIC authorization
        // This checks database in REAL-TIME for every request
        auth.requestMatchers("/api/**")
                .access(dynamicAuthorizationManager);

        // ✅ 7. Deny all other requests
        auth.anyRequest().denyAll();

        log.info("✅ Security configuration completed with dynamic authorization");
    }
}
