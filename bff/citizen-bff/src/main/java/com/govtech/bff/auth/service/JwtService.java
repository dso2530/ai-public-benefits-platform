package com.govtech.bff.auth.service;

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

  @Value("${security.jwt.secret}")
  private String secret;

  public String generateToken(String subject, String email) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    return Jwts.builder()
        .subject(subject)
        .issuer("citizen-bff")
        .claim("email", email)
        .issuedAt(new Date())
        .expiration(Date.from(Instant.now().plus(8, ChronoUnit.HOURS)))
        .signWith(key, Jwts.SIG.HS384)
        .compact();
  }
}
