package com.infoevent.authservice.clients;

import com.infoevent.authservice.entities.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@FeignClient(name = "USER-SERVICE")
public interface UserRestClient {
    @GetMapping("/users/{id}")
    @CircuitBreaker(name = "userservice", fallbackMethod = "getDefaultUser")
    User getUserById(@PathVariable Long id);
    @GetMapping("/users/by-email")
    @CircuitBreaker(name = "userservice", fallbackMethod = "getDefaultUserEmail")
    User getUserByEmail(@RequestParam String email);

    @PostMapping("/users")
    @CircuitBreaker(name = "userservice")
    User createUser(@Valid @RequestBody User user);

    default User getDefaultUser(Long id, Throwable throwable){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID: " + id + " could not be retrieved.", throwable);
    }
    default User getDefaultUserEmail(String email, Throwable throwable){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with Email: " + email + " could not be retrieved.", throwable);
    }
    default User createDefaultUser(User user, Throwable throwable){
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "User could not be created due to service unavailability.", throwable);
    }
}
