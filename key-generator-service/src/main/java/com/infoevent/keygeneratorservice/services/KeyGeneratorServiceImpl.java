package com.infoevent.keygeneratorservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeyGeneratorServiceImpl implements KeyGeneratorService{

    private final SecureRandom secureRandom;
    private  final Base64.Encoder base64Encoder;
    @Override
    public String generateSecureRandomKey() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
