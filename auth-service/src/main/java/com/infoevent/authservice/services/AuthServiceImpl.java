package com.infoevent.authservice.services;

import com.infoevent.authservice.clients.UserRestClient;
import com.infoevent.authservice.entities.AuthRequest;
import com.infoevent.authservice.entities.AuthResponse;
import com.infoevent.authservice.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRestClient userRestClient;

    public AuthResponse register(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        User registeredUser = userRestClient.createUser(user);

        String accessToken = jwtService.generate(registeredUser, "ACCESS");
        String refreshToken = jwtService.generate(registeredUser, "REFRESH");

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRestClient.getUserByEmail(request.getEmail());

        if (user != null && BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            String accessToken = jwtService.generate(user, "ACCESS");
            String refreshToken = jwtService.generate(user, "REFRESH");
            return new AuthResponse(accessToken, refreshToken);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

}
