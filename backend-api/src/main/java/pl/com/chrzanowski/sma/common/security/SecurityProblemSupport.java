package pl.com.chrzanowski.sma.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import pl.com.chrzanowski.sma.common.exception.ForbiddenException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class SecurityProblemSupport implements AuthenticationEntryPoint, AccessDeniedHandler {
    private final HandlerExceptionResolver resolver;

    Logger log = LoggerFactory.getLogger(SecurityProblemSupport.class);

    @Autowired
    public SecurityProblemSupport(@Qualifier("handlerExceptionResolver") final HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        if (authException instanceof BadCredentialsException) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            Map<String, Object> body = Map.of(
                    "timestamp", LocalDateTime.now().toString(),
                    "code", "unauthorized",
                    "message", authException.getMessage(),
                    "details", Map.of("path", request.getRequestURI())
            );

            new ObjectMapper().writeValue(response.getOutputStream(), body);
            return; // Ensure nothing else is processed
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        resolver.resolveException(request, response, null, authException);
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("SecurityProblemSupport: handle called with exception: {}", accessDeniedException.getMessage());
        throw new ForbiddenException("Brak dostępu: " + accessDeniedException.getMessage(), Map.of("path", request.getRequestURI()));
    }
}
