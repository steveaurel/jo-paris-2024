package com.infoevent.ticketservice.client;

import com.infoevent.ticketservice.entities.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserRestClient {
    @GetMapping("/users/{id}")
    @CircuitBreaker(name = "userservice", fallbackMethod = "getDefaultUser")
    User getUserById(@PathVariable Long id);

    default User getDefaultUser(Long id, Exception e){
        return User.builder()
                .id(id)
                .firstName("source not available")
                .lastName("source not available")
                .email("source not available")
                .lastName("source not available")
                .build();
    }
}
