package com.dessima.gambia.adapters.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImovelRepository extends JpaRepository<ImovelEntity, UUID> {
  List<ImovelEntity> findByUsuarioId(UUID usuarioId);
}
