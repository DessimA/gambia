package com.dessima.gambia.adapters.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnaliseConsumoRepository extends JpaRepository<AnaliseConsumoEntity, UUID> {
  List<AnaliseConsumoEntity> findByImovelIdInOrderByCreatedAtDesc(List<UUID> imovelIds);
}
