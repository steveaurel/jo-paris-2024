package com.infoevent.ticketservice.client;

import com.infoevent.ticketservice.entities.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@FeignClient(name = "USER-SERVICE")
public interface UserRestClient {
    @GetMapping("/users/{id}")
    @CircuitBreaker(name = "userservice", fallbackMethod = "getDefaultUser")
    User getUserById(@PathVariable Long id);

    default User getDefaultUser(Long id, Throwable throwable){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID: " + id + " could not be retrieved.", throwable);
    }
}
