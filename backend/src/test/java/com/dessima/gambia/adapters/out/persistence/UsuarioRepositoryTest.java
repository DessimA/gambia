package com.dessima.gambia.adapters.out.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

  @Autowired private UsuarioRepository usuarioRepository;

  @Test
  void deveSalvarEBuscarPorEmail() {
    var id = UUID.randomUUID();
    var entity = new UsuarioEntity(id, "Teste", "test@test.com", "hash123");
    usuarioRepository.save(entity);

    Optional<UsuarioEntity> found = usuarioRepository.findByEmail("test@test.com");

    assertTrue(found.isPresent());
    assertEquals(id, found.get().getId());
    assertEquals("Teste", found.get().getNome());
    assertEquals("test@test.com", found.get().getEmail());
    assertEquals("hash123", found.get().getSenhaHash());
    assertNotNull(found.get().getCreatedAt());
  }

  @Test
  void deveRetornarEmpty_QuandoEmailNaoExiste() {
    Optional<UsuarioEntity> found = usuarioRepository.findByEmail("nao.existe@test.com");
    assertTrue(found.isEmpty());
  }

  @Test
  void deveBuscarPorId() {
    var id = UUID.randomUUID();
    var entity = new UsuarioEntity(id, "Teste", "test@test.com", "hash123");
    usuarioRepository.save(entity);

    Optional<UsuarioEntity> found = usuarioRepository.findById(id);

    assertTrue(found.isPresent());
    assertEquals("test@test.com", found.get().getEmail());
  }

  @Test
  void deveLancarExcecao_QuandoEmailDuplicado() {
    var id1 = UUID.randomUUID();
    var id2 = UUID.randomUUID();
    usuarioRepository.saveAndFlush(new UsuarioEntity(id1, "User1", "dup@test.com", "hash1"));

    assertThrows(
        Exception.class,
        () -> {
          usuarioRepository.saveAndFlush(new UsuarioEntity(id2, "User2", "dup@test.com", "hash2"));
        });
  }
}
