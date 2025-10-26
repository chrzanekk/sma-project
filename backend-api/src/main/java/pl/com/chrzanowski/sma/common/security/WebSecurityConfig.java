package pl.com.chrzanowski.sma.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.com.chrzanowski.sma.common.security.jwt.AuthTokenFilter;
import pl.com.chrzanowski.sma.common.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    UserDetailsServiceImpl userDetailsService;
    private final SecurityProblemSupport securityProblemSupport;
    private final DynamicSecurityConfigurer dynamicSecurityConfigurer;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService,
                             SecurityProblemSupport securityProblemSupport, DynamicSecurityConfigurer dynamicSecurityConfigurer) {
        this.userDetailsService = userDetailsService;
        this.securityProblemSupport = securityProblemSupport;
        this.dynamicSecurityConfigurer = dynamicSecurityConfigurer;
    }

    @Bean
    public AuthTokenFilter authenticationTJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // ✅ Dynamiczna konfiguracja z bazy danych
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(config -> {
                    config.authenticationEntryPoint(securityProblemSupport);
                    config.accessDeniedHandler(securityProblemSupport);
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(dynamicSecurityConfigurer::configureAuthorization)
                .httpBasic(Customizer.withDefaults());
        http.addFilterBefore(authenticationTJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
