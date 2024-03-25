package com.infoevent.gatewayservice.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

/**
 * Interface defining utility methods for working with JSON Web Tokens (JWT).
 * Provides methods to extract claims from a token and to check if a token is expired.
 */
public interface JwtUtils {

    /**
     * Extracts the claims (payload) from a JWT token.
     * This method is used to parse the token and retrieve its contained claims,
     * which include information such as the token subject (typically the user ID),
     * issuance and expiration times, and other custom claims.
     *
     * @param token The JWT token from which claims are to be extracted.
     * @return Claims The claims contained within the given JWT token.
     * @throws io.jsonwebtoken.JwtException if any problem occurs during parsing the token.
     */
    Claims getClaims(String token);

    /**
     * Checks if the provided JWT token is expired.
     * This is typically determined by comparing the token's expiration claim
     * against the current date and time.
     *
     * @param token The JWT token to check for expiration.
     * @return boolean True if the token is expired, false otherwise.
     */
    boolean isExpired(String token);

    /**
     * Validates the integrity and structure of the provided JWT token.
     * This involves verifying the token's signature, ensuring it matches the expected format,
     * and confirming that it hasn't been tampered with. This method should be used
     * to ascertain that a token is both valid and trustworthy before extracting any information from it.
     *
     * @param token The JWT token to be validated.
     * @return boolean True if the token is valid and has a legitimate signature, false otherwise.
     * @throws JwtException if the token is invalid or any problem occurs during validation.
     */
     boolean validateToken(final String token);
}