package com.dessima.gambia.domain.ports.in;

import com.dessima.gambia.domain.model.AnaliseHistorico;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HistoricoUseCase {
  List<AnaliseHistorico> listarAnalises(UUID usuarioId);

  Optional<AnaliseHistorico> buscarAnalise(UUID analiseId);
}
