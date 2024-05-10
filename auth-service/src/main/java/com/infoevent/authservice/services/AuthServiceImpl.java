package com.infoevent.authservice.services;

import com.infoevent.authservice.clients.UserRestClient;
import com.infoevent.authservice.entities.AuthRequest;
import com.infoevent.authservice.entities.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRestClient userRestClient;

    public User register(User user) {
        log.info("Registering new user with email: {}", user.getEmail());
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        User registeredUser = userRestClient.createUser(user);

        String accessToken = jwtService.generate(registeredUser, "ACCESS");
        String refreshToken = jwtService.generate(registeredUser, "REFRESH");

        registeredUser.setAccessToken(accessToken);
        registeredUser.setRefreshToken(refreshToken);

        log.info("User registered successfully with email: {}", user.getEmail());
        return registeredUser;
    }

    public User login(AuthRequest request) {
        log.info("Attempting login for email: {}", request.getEmail());
        User user = userRestClient.getUserByEmail(request.getEmail());

        if (user != null && BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            String accessToken = jwtService.generate(user, "ACCESS");
            String refreshToken = jwtService.generate(user, "REFRESH");
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            log.info("Login successful for email: {}", request.getEmail());
            return user;
        } else {
            log.warn("Invalid login attempt for email: {}", request.getEmail());
            throw new RuntimeException("Invalid credentials");
        }
    }

    public void validateToken(String token) {
        log.info("Validating token");
        jwtService.validateToken(token);
        log.info("Token validated successfully");
    }
}
