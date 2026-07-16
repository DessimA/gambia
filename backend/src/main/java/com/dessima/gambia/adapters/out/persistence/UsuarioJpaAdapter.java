package com.dessima.gambia.adapters.out.persistence;

import com.dessima.gambia.domain.model.Usuario;
import com.dessima.gambia.domain.ports.out.UsuarioRepositoryPort;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UsuarioJpaAdapter implements UsuarioRepositoryPort {

  private final UsuarioRepository repository;

  public UsuarioJpaAdapter(UsuarioRepository repository) {
    this.repository = repository;
  }

  @Override
  public Usuario salvar(Usuario usuario) {
    var entity =
        new UsuarioEntity(usuario.id(), usuario.nome(), usuario.email(), usuario.senhaHash());
    repository.save(entity);
    return usuario;
  }

  @Override
  public Optional<Usuario> buscarPorEmail(String email) {
    return repository
        .findByEmail(email)
        .map(
            e ->
                new Usuario(
                    e.getId(), e.getNome(), e.getEmail(), e.getSenhaHash(), e.getCreatedAt()));
  }

  @Override
  public Optional<Usuario> buscarPorId(UUID id) {
    return repository
        .findById(id)
        .map(
            e ->
                new Usuario(
                    e.getId(), e.getNome(), e.getEmail(), e.getSenhaHash(), e.getCreatedAt()));
  }
}
