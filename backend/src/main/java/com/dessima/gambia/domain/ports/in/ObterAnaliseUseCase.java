package com.dessima.gambia.domain.ports.in;

import com.dessima.gambia.domain.model.ResultadoAnalise;
import com.dessima.gambia.domain.model.SolicitacaoAnalise;

public interface ObterAnaliseUseCase {
  ResultadoAnalise executar(SolicitacaoAnalise solicitacao);
}
