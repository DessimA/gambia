package com.dessima.gambia.adapters.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecomendacaoGeradaRepository
    extends JpaRepository<RecomendacaoGeradaEntity, UUID> {
  List<RecomendacaoGeradaEntity> findByAnaliseId(UUID analiseId);
}
