package com.govtech.citizen.auth.service;

import com.govtech.citizen.auth.dto.KeycloakIdentity;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakIdentityService {

    private final JwtDecoder keycloakJwtDecoder;

    public KeycloakIdentity extractIdentity(
            String idToken) {

        try {

            Jwt jwt = keycloakJwtDecoder.decode(idToken);

            String subject = jwt.getSubject();

            String email = jwt.getClaimAsString("email");

            String fullName = jwt.getClaimAsString("name");

            if (subject == null
                    || subject.isBlank()) {

                throw new IllegalStateException(
                        "Missing subject in Keycloak ID token");
            }

            if (email == null
                    || email.isBlank()) {

                throw new IllegalStateException(
                        "Missing email in Keycloak ID token");
            }

            Instant expiresAt = jwt.getExpiresAt();

            if (expiresAt == null) {

                throw new IllegalStateException(
                        "Missing expiration in Keycloak ID token");
            }

            long expiresIn = Math.max(
                    0,
                    expiresAt
                            .getEpochSecond()
                            - Instant.now().getEpochSecond());

            log.info(
                    "Keycloak identity validated: subject={}, email={}, expiresIn={}s",
                    subject,
                    maskEmail(email),
                    expiresIn);

            return new KeycloakIdentity(
                    subject,
                    email,
                    fullName);

        } catch (Exception exception) {

            log.error(
                    "Keycloak ID token validation failed",
                    exception);

            throw new IllegalStateException(
                    "Invalid Keycloak ID token",
                    exception);
        }
    }

    private String maskEmail(
            String email) {

        int at = email.indexOf('@');

        if (at <= 1) {
            return "***";
        }

        return email.charAt(0)
                + "***"
                + email.substring(at);
    }
}