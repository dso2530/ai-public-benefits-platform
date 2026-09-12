package com.govtech.bff.security.auth.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public final class JwtTokenGenerator {

    private static final String ISSUER = "citizen-bff";

    private JwtTokenGenerator() {
    }

    public static String generate(
            String secret,
            String subject,
            String email) {

        Instant now = Instant.now();
        Instant expiration = now.plus(8, ChronoUnit.HOURS);

        SecretKey key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(subject)
                .issuer(ISSUER)
                .claim("email", email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(key, Jwts.SIG.HS384)
                .compact();
    }

    public static void main(String[] args) {

        String secret = System.getenv("JWT_SECRET");

        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                    "JWT_SECRET environment variable is required");
        }

        String token = generate(
                secret,
                "test-user",
                "test@example.com");

        System.out.println(token);
    }
}