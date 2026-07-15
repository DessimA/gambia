package com.dessima.gambia.adapters.in.web;

import com.dessima.gambia.adapters.in.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  private final JwtTokenProvider tokenProvider;

  public AuthController(JwtTokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @GetMapping("/login")
  public ResponseEntity<Void> login(HttpServletResponse response) {
    String token = tokenProvider.gerarToken("dev-user");

    var cookie = new jakarta.servlet.http.Cookie("SESSION_TOKEN", token);
    cookie.setHttpOnly(true);
    cookie.setSecure(false);
    cookie.setPath("/");
    cookie.setMaxAge(86400);
    response.addCookie(cookie);

    return ResponseEntity.ok().build();
  }
}
