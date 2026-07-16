package com.dessima.gambia.domain.service;

import com.dessima.gambia.domain.model.Usuario;
import com.dessima.gambia.domain.ports.in.AutenticacaoUseCase;
import com.dessima.gambia.domain.ports.out.UsuarioRepositoryPort;
import java.time.Instant;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AutenticacaoService implements AutenticacaoUseCase {

  private final UsuarioRepositoryPort usuarioRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public AutenticacaoService(
      UsuarioRepositoryPort usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Usuario cadastrar(String nome, String email, String senha) {
    if (usuarioRepository.buscarPorEmail(email).isPresent()) {
      throw new IllegalArgumentException("E-mail ja cadastrado");
    }

    var usuario =
        new Usuario(UUID.randomUUID(), nome, email, passwordEncoder.encode(senha), Instant.now());
    return usuarioRepository.salvar(usuario);
  }

  @Override
  public Usuario login(String email, String senha) {
    var usuario =
        usuarioRepository
            .buscarPorEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha invalidos"));

    if (!passwordEncoder.matches(senha, usuario.senhaHash())) {
      throw new IllegalArgumentException("E-mail ou senha invalidos");
    }

    return usuario;
  }
}
