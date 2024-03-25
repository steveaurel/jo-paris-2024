package com.infoevent.authservice.services;

import com.infoevent.authservice.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * Service for JWT token operations, including creation, parsing, and validation.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private Key key;

    /**
     * Initializes the signing key for JWT operations based on the configured secret.
     */
    @PostConstruct
    public void initKey() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Extracts the claims (payload) from a JWT token.
     *
     * @param token The JWT token.
     * @return Claims The claims contained within the token.
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /**
     * Retrieves the expiration date of a JWT token.
     *
     * @param token The JWT token.
     * @return Date The expiration date of the token.
     */
    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    /**
     * Generates a new JWT token for a user with specific claims.
     *
     * @param user The user for whom the token is being generated.
     * @param tokenType The type of token being generated, influencing expiration time.
     * @return String A JWT token.
     */
    public String generate(User user, String tokenType) {
        Map<String, Object> claims = Map.of("id", user.getId().toString(), "role", user.getRole().toString());
        long expMillis = "ACCESS".equalsIgnoreCase(tokenType)
                ? Long.parseLong(expiration) * 1000
                : Long.parseLong(expiration) * 1000 * 5;

        final Date now = new Date();
        final Date exp = new Date(now.getTime() + expMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }

    /**
     * Checks if a JWT token has expired.
     *
     * @param token The JWT token.
     * @return boolean True if the token has expired, false otherwise.
     */
    private boolean isExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    /**
     * Validates a JWT token's signature and structure.
     *
     * @param token The JWT token to validate.
     */
    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
