package com.infoevent.userservice.clients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "KEY-GENERATOR-SERVICE")
public interface KeyGeneratorRestClient {

    @GetMapping("/generate-key")
    @CircuitBreaker(name = "keygeneratorservice", fallbackMethod = "getDefaultKey")
    String getKeyGenerator();

    default String getDefaultKey(){
        return "";
    }
}
