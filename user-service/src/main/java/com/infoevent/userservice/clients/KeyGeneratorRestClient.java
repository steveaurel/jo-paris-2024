package com.infoevent.userservice.clients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@FeignClient(name = "KEY-GENERATOR-SERVICE")
public interface KeyGeneratorRestClient {

    @GetMapping("/generate-key")
    @CircuitBreaker(name = "keygeneratorservice", fallbackMethod = "getDefaultKey")
    String getKeyGenerator();

    default String getDefaultKey(Throwable throwable) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Key generation failed", throwable);
    }
}
