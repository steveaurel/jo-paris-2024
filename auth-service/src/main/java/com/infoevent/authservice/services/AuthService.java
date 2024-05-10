package com.infoevent.authservice.services;

import com.infoevent.authservice.entities.AuthRequest;
import com.infoevent.authservice.entities.User;

/**
 * Service interface for authentication operations.
 * This includes registering new users, logging in, and validating JWT tokens.
 */
public interface AuthService {

    /**
     * Registers a new user in the system.
     * This method is responsible for validating the provided user details,
     * encrypting the user's password, and storing the user information.
     *
     * @param user The user information to register.
     * @return An {@link User} containing the JWT token and other relevant authentication details.
     */
    User register(User user);

    /**
     * Authenticates a user based on the provided credentials.
     * This method verifies the user's credentials, generates a JWT token if authentication is successful,
     * and returns the token along with any other necessary authentication details.
     *
     * @param request The authentication request containing the username and password.
     * @return An {@link User} containing the JWT token and other relevant authentication details.
     */
    User login(AuthRequest request);

    /**
     * Validates the provided JWT token.
     * This method checks if the token is valid, not expired, and issued by the server.
     * It throws an exception or returns a boolean value indicating the token's validity.
     *
     * @param token The JWT token to validate.
     */
    void validateToken(String token);
}
