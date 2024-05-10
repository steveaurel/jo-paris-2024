package com.infoevent.authservice.controllers;

import com.infoevent.authservice.entities.AuthRequest;
import com.infoevent.authservice.entities.User;
import com.infoevent.authservice.services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling authentication requests.
 * Provides endpoints for user registration, login, and token validation.
 */
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint for user registration.
     *
     * @param user The user details submitted for registration.
     * @return ResponseEntity containing the authentication response (e.g., JWT token) if registration is successful.
     */
    @PostMapping(value = "/sign-up")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        log.info("Received request to register user: {}", user.getEmail()); // Log the username attempting to register
        return ResponseEntity.ok(authService.register(user));
    }

    /**
     * Endpoint for user login.
     *
     * @param request The authentication request containing the username and password.
     * @return ResponseEntity containing the authentication response (e.g., JWT token) if login is successful.
     */
    @PostMapping(value = "/sign-in")
    public ResponseEntity<User> login(@Valid @RequestBody AuthRequest request) {
        log.info("Received login request for user: {}", request.getEmail()); // Log the username attempting to log in
        try {
            User user = authService.login(request);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Login attempt failed for user: {}. Error: {}", request.getEmail(), e.getMessage()); // Log the error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Endpoint for validating a JWT token.
     *
     * @param token The JWT token to be validated.
     * @return A string message indicating whether the token is valid or not.
     */
    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        log.info("Received request to validate token"); // Log the token validation attempt
        try {
            authService.validateToken(token);
            return "Token is valid";
        } catch (Exception e) {
            log.error("Token validation failed. Error: {}", e.getMessage()); // Log the error if token validation fails
            return "Token is invalid";
        }
    }
}
