package com.dessima.gambia.domain.ports.out;

import com.dessima.gambia.domain.model.ResultadoAnalise;
import com.dessima.gambia.domain.model.SolicitacaoAnalise;

public interface MLClientPort {
    ResultadoAnalise classificar(SolicitacaoAnalise solicitacao);
}
