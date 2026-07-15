package com.dessima.gambia.adapters.in.dto;

import com.dessima.gambia.domain.model.ResultadoAnalise;
import java.math.BigDecimal;
import java.util.List;

public record AnaliseResponse(
    String categoria,
    BigDecimal probabilidade,
    List<String> recomendacoes,
    BigDecimal custo_estimado_mensal) {
  public static AnaliseResponse fromDomain(ResultadoAnalise domain) {
    return new AnaliseResponse(
        domain.categoria().name(),
        domain.probabilidade(),
        domain.recomendacoes(),
        domain.custoEstimadoMensal());
  }
}
