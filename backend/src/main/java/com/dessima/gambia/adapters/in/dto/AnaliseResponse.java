package com.dessima.gambia.adapters.in.dto;

import java.math.BigDecimal;
import java.util.List;

import com.dessima.gambia.domain.model.ResultadoAnalise;

public record AnaliseResponse(
        String categoria,
        BigDecimal probabilidade,
        List<String> recomendacoes,
        BigDecimal custo_estimado_mensal
) {
    public static AnaliseResponse fromDomain(ResultadoAnalise domain) {
        return new AnaliseResponse(
                domain.categoria().name(),
                domain.probabilidade(),
                domain.recomendacoes(),
                domain.custoEstimadoMensal());
    }
}
