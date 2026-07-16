package com.dessima.gambia.domain.service;

import com.dessima.gambia.domain.model.AnaliseHistorico;
import com.dessima.gambia.domain.ports.in.HistoricoUseCase;
import com.dessima.gambia.domain.ports.out.PersistenciaPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HistoricoService implements HistoricoUseCase {

  private final PersistenciaPort persistencia;

  public HistoricoService(PersistenciaPort persistencia) {
    this.persistencia = persistencia;
  }

  @Override
  public List<AnaliseHistorico> listarAnalises(UUID usuarioId) {
    return persistencia.buscarAnalisesPorUsuario(usuarioId);
  }

  @Override
  public Optional<AnaliseHistorico> buscarAnalise(UUID analiseId) {
    return persistencia.buscarAnalisePorId(analiseId);
  }
}
