package com.dessima.gambia.adapters.in.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private final SecretKey secretKey;
  private final long expirationMs;

  public JwtTokenProvider(
      @Value("${app.jwt.secret-key}") String secret,
      @Value("${app.jwt.expiration-ms}") long expirationMs) {
    byte[] keyBytes = Base64.getEncoder().encodeToString(secret.getBytes()).getBytes();
    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    this.expirationMs = expirationMs;
  }

  public String gerarToken(String subject) {
    var now = Instant.now();
    return Jwts.builder()
        .subject(subject)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusMillis(expirationMs)))
        .signWith(secretKey)
        .compact();
  }

  public boolean validarToken(String token) {
    try {
      obterClaims(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  public String obterSubject(String token) {
    return obterClaims(token).getSubject();
  }

  private Claims obterClaims(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
  }
}
