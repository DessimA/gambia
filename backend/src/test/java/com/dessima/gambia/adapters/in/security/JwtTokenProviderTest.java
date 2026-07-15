package com.dessima.gambia.adapters.in.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

  private JwtTokenProvider tokenProvider;

  @BeforeEach
  void setUp() {
    tokenProvider =
        new JwtTokenProvider("minha_chave_secreta_super_segura_de_32_caracteres", 3600000);
  }

  @Test
  void deveGerarTokenValido() {
    String token = tokenProvider.gerarToken("user-123");
    assertNotNull(token);
    assertTrue(tokenProvider.validarToken(token));
  }

  @Test
  void deveExtrairSubjectDoToken() {
    String token = tokenProvider.gerarToken("user-123");
    assertEquals("user-123", tokenProvider.obterSubject(token));
  }

  @Test
  void deveRejeitarTokenInvalido() {
    assertFalse(tokenProvider.validarToken("token.invalido.aqui"));
  }

  @Test
  void deveRejeitarTokenNulo() {
    assertThrows(IllegalArgumentException.class, () -> tokenProvider.validarToken(null));
  }

  @Test
  void deveRejeitarTokenVazio() {
    assertThrows(IllegalArgumentException.class, () -> tokenProvider.validarToken(""));
  }

  @Test
  void deveGerarTokensDiferentesParaSubjectsDiferentes() {
    String token1 = tokenProvider.gerarToken("user-1");
    String token2 = tokenProvider.gerarToken("user-2");
    assertNotEquals(token1, token2);
  }
}
