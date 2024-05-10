package com.infoevent.keygeneratorservice.controllers;

import com.infoevent.keygeneratorservice.services.KeyGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/generate-key")
@Slf4j
public class KeyGeneratorController {

    private final KeyGeneratorService keyGenerationService;

    @GetMapping("")
    public String generateKey() {
        return keyGenerationService.generateSecureRandomKey();
    }
}
