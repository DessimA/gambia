package com.dessima.gambia.adapters.out.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {
  Optional<UsuarioEntity> findByEmail(String email);
}
