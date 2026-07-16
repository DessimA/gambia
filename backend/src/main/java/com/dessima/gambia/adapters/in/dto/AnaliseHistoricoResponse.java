package com.dessima.gambia.adapters.in.dto;

import com.dessima.gambia.domain.model.AnaliseHistorico;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AnaliseHistoricoResponse(
    UUID id,
    String categoria,
    BigDecimal probabilidade,
    BigDecimal consumoKwh,
    BigDecimal custoEstimadoMensal,
    BigDecimal emissaoCo2Kg,
    boolean usoHorarioPico,
    int horasAltoConsumo,
    Instant createdAt,
    List<String> recomendacoes) {

  public static AnaliseHistoricoResponse fromDomain(AnaliseHistorico domain) {
    return new AnaliseHistoricoResponse(
        domain.id(),
        domain.categoria(),
        domain.probabilidade(),
        domain.consumoKwh(),
        domain.custoEstimadoMensal(),
        domain.emissaoCo2Kg(),
        domain.usoHorarioPico(),
        domain.horasAltoConsumo(),
        domain.createdAt(),
        domain.recomendacoes());
  }
}
