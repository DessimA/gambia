package com.dessima.gambia.domain.ports.out;

import com.dessima.gambia.domain.model.AnaliseHistorico;
import com.dessima.gambia.domain.model.ResultadoAnalise;
import com.dessima.gambia.domain.model.SolicitacaoAnalise;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersistenciaPort {
  UUID salvarImovel(SolicitacaoAnalise solicitacao);

  UUID salvarAnalise(UUID imovelId, SolicitacaoAnalise solicitacao, ResultadoAnalise resultado);

  void salvarRecomendacoes(UUID analiseId, ResultadoAnalise resultado);

  List<AnaliseHistorico> buscarAnalisesPorUsuario(UUID usuarioId);

  Optional<AnaliseHistorico> buscarAnalisePorId(UUID analiseId);
}
