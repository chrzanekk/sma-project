package pl.com.chrzanowski.sma.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
//                        .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/confirm",
//                                "/api/auth/request-password-reset", "/api/auth/reset-password").permitAll()
//
//                        //ACCOUNT CONTROLLER
//                        .requestMatchers("/api/account/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
//
//                        //USER CONTROLLER
//                        .requestMatchers("/api/user/**").hasAnyAuthority("ROLE_ADMIN")
//
//                        //ROLE CONTROLLER
//                        .requestMatchers("/api/role/**").hasAnyAuthority("ROLE_ADMIN")
//
//                        //CONTRACTOR CONTROLLER
//                        .requestMatchers("/api/contractors/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
//
//                        //CONTACT CONTROLLER
//                        .requestMatchers("/api/contacts/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
//
//                        //COMPANY CONTROLLER
//                        .requestMatchers("/api/companies/**").hasAnyAuthority("ROLE_ADMIN")
//
//                        //CONSTRUCTION SITE CONTROLLER
//                        .requestMatchers("/api/construction-sites/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
//
//                        // POSITION CONTROLLER
//                        .requestMatchers("/api/positions/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
//
//                        // CONTRACT CONTROLLER
//                        .requestMatchers("/api/contracts/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
//
//
//                        .anyRequest().hasAnyAuthority("ROLE_ADMIN")
                .httpBasic(Customizer.withDefaults());
        http.addFilterBefore(authenticationTJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
