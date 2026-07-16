package com.dessima.gambia.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.dessima.gambia.domain.model.Usuario;
import com.dessima.gambia.domain.ports.out.UsuarioRepositoryPort;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {

  @Mock private UsuarioRepositoryPort usuarioRepository;
  @Mock private BCryptPasswordEncoder passwordEncoder;

  private AutenticacaoService service;

  @BeforeEach
  void setUp() {
    service = new AutenticacaoService(usuarioRepository, passwordEncoder);
  }

  @Test
  void deveCadastrarComSucesso() {
    when(usuarioRepository.buscarPorEmail("test@test.com")).thenReturn(Optional.empty());
    when(passwordEncoder.encode("senha123")).thenReturn("hash_criptografado");
    when(usuarioRepository.salvar(any())).thenAnswer(i -> i.getArgument(0));

    Usuario usuario = service.cadastrar("Teste", "test@test.com", "senha123");

    assertNotNull(usuario.id());
    assertEquals("Teste", usuario.nome());
    assertEquals("test@test.com", usuario.email());
    assertEquals("hash_criptografado", usuario.senhaHash());
    assertNotNull(usuario.createdAt());
  }

  @Test
  void deveLancarExcecao_QuandoEmailJaCadastrado() {
    var existente =
        new Usuario(UUID.randomUUID(), "Existente", "test@test.com", "hash", Instant.now());
    when(usuarioRepository.buscarPorEmail("test@test.com")).thenReturn(Optional.of(existente));

    var ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> service.cadastrar("Teste", "test@test.com", "senha123"));
    assertEquals("E-mail ja cadastrado", ex.getMessage());
  }

  @Test
  void deveFazerLoginComSucesso() {
    var usuario = new Usuario(UUID.randomUUID(), "Teste", "test@test.com", "hash", Instant.now());
    when(usuarioRepository.buscarPorEmail("test@test.com")).thenReturn(Optional.of(usuario));
    when(passwordEncoder.matches("senha123", "hash")).thenReturn(true);

    Usuario resultado = service.login("test@test.com", "senha123");

    assertEquals(usuario, resultado);
  }

  @Test
  void deveLancarExcecao_QuandoEmailNaoExiste() {
    when(usuarioRepository.buscarPorEmail("inexistente@test.com")).thenReturn(Optional.empty());

    var ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> service.login("inexistente@test.com", "senha123"));
    assertEquals("E-mail ou senha invalidos", ex.getMessage());
  }

  @Test
  void deveLancarExcecao_QuandoSenhaInvalida() {
    var usuario = new Usuario(UUID.randomUUID(), "Teste", "test@test.com", "hash", Instant.now());
    when(usuarioRepository.buscarPorEmail("test@test.com")).thenReturn(Optional.of(usuario));
    when(passwordEncoder.matches("senha_errada", "hash")).thenReturn(false);

    var ex =
        assertThrows(
            IllegalArgumentException.class, () -> service.login("test@test.com", "senha_errada"));
    assertEquals("E-mail ou senha invalidos", ex.getMessage());
  }
}
