package com.govtech.bff.security.auth.service;

import com.govtech.bff.security.auth.model.InternalAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private static final String ISSUER = "citizen-bff";
  private static final long EXPIRATION_HOURS = 8;

  @Value("${security.jwt.secret}")
  private String secret;

  public InternalAuthentication generateToken(
      String subject,
      String email) {

    Instant now = Instant.now();

    Instant expiration = now.plus(
        EXPIRATION_HOURS,
        ChronoUnit.HOURS);

    String accessToken = Jwts.builder()
        .subject(subject)
        .issuer(ISSUER)
        .claim("email", email)
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiration))
        .signWith(
            getSigningKey(),
            Jwts.SIG.HS384)
        .compact();

    long expiresIn = expiration.getEpochSecond()
        - now.getEpochSecond();

    return InternalAuthentication.builder()
        .accessToken(accessToken)
        .expiresIn(expiresIn)
        .build();
  }

  public Jws<Claims> parseToken(String token) {

    return Jwts.parser()
        .verifyWith(getSigningKey())
        .requireIssuer(ISSUER)
        .build()
        .parseSignedClaims(token);
  }

  private SecretKey getSigningKey() {

    return Keys.hmacShaKeyFor(
        secret.getBytes(StandardCharsets.UTF_8));
  }
}