package com.dessima.gambia.domain.ports.out;

import com.dessima.gambia.domain.model.Usuario;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositoryPort {
  Usuario salvar(Usuario usuario);

  Optional<Usuario> buscarPorEmail(String email);

  Optional<Usuario> buscarPorId(UUID id);
}
