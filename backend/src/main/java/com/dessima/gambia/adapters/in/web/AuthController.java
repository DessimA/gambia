package com.dessima.gambia.adapters.in.web;

import com.dessima.gambia.adapters.in.dto.CadastroRequest;
import com.dessima.gambia.adapters.in.dto.LoginRequest;
import com.dessima.gambia.adapters.in.dto.LoginResponse;
import com.dessima.gambia.adapters.in.security.JwtTokenProvider;
import com.dessima.gambia.domain.ports.in.AutenticacaoUseCase;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  private final JwtTokenProvider tokenProvider;
  private final AutenticacaoUseCase autenticacaoUseCase;

  public AuthController(JwtTokenProvider tokenProvider, AutenticacaoUseCase autenticacaoUseCase) {
    this.tokenProvider = tokenProvider;
    this.autenticacaoUseCase = autenticacaoUseCase;
  }

  @PostMapping("/auth/cadastrar")
  public ResponseEntity<Void> cadastrar(@Valid @RequestBody CadastroRequest request) {
    autenticacaoUseCase.cadastrar(request.nome(), request.email(), request.senha());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/auth/login")
  public ResponseEntity<LoginResponse> login(
      @Valid @RequestBody LoginRequest request, HttpServletResponse response) {
    var usuario = autenticacaoUseCase.login(request.email(), request.senha());
    String token = tokenProvider.gerarToken(usuario.id().toString());

    var cookie = new jakarta.servlet.http.Cookie("SESSION_TOKEN", token);
    cookie.setHttpOnly(true);
    cookie.setSecure(false);
    cookie.setPath("/");
    cookie.setMaxAge(86400);
    response.addCookie(cookie);

    return ResponseEntity.ok(new LoginResponse(usuario.id(), usuario.nome(), usuario.email()));
  }

  @GetMapping("/login")
  public ResponseEntity<Void> loginDev(HttpServletResponse response) {
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
