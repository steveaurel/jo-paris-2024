package com.infoevent.gatewayservice.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class JwtUtilsImpl implements JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void initKey() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        log.info("JWT Key initialized");
    }

    @Override
    public Claims getClaims(String token) {
        log.info("Extracting claims from token");
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    @Override
    public boolean isExpired(String token) {
        try {
            boolean isExpired = getClaims(token).getExpiration().before(new Date());
            log.info("Checking if token is expired: {}", isExpired);
            return isExpired;
        } catch (Exception e) {
            log.error("Error checking token expiration", e);
            return false;
        }
    }

    @Override
    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

}