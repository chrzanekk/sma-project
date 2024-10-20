package pl.com.chrzanowski.scma.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.scma.security.service.UserDetailsImpl;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;


@Component
public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    private static final String AUTHORITIES_KEY = "auth";

    @Value("${scaffolding-app.jwtSecret}")
    private String jwtSecret;

    @Value("${scaffolding-app.jwtExpirationMs}")
    private Long jwtExpirationMs;


    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + jwtExpirationMs);

        return Jwts.builder()
                .claim(AUTHORITIES_KEY, authorities)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(validity)
                .signWith(key())
                .compact();
    }


    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().decryptWith((SecretKey) key()).build().parseSignedClaims(token).getPayload().getSubject();
    }


}
