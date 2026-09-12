package com.govtech.citizen.auth.oauth;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class PkceService {

    public PkceChallenge generate() {

        String verifier = generateVerifier();

        String challenge = generateChallenge(verifier);

        return new PkceChallenge(
                verifier,
                challenge);
    }

    private String generateVerifier() {

        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String generateChallenge(String verifier) {

        try {
            byte[] digest = MessageDigest
                    .getInstance("SHA-256")
                    .digest(verifier.getBytes(StandardCharsets.US_ASCII));

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(digest);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 algorithm not available",
                    e);
        }
    }
}