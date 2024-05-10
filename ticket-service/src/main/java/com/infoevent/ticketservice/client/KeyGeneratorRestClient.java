package com.infoevent.ticketservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@FeignClient(name = "KEY-GENERATOR-SERVICE")
public interface KeyGeneratorRestClient {

    @GetMapping("/generate-key")
    @CircuitBreaker(name = "keygeneratorservice", fallbackMethod = "getDefaultKey")
    String getKeyGenerator();  // Continues to attempt to generate a key

    default String getDefaultKey(Throwable throwable) {
        // Throws an exception instead of returning an empty string
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Key generation failed", throwable);
    }
}
