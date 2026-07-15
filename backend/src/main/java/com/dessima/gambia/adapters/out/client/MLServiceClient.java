package com.dessima.gambia.adapters.out.client;

import java.math.BigDecimal;
import java.util.List;

import com.dessima.gambia.domain.model.ClassificacaoEficiencia;
import com.dessima.gambia.domain.model.ResultadoAnalise;
import com.dessima.gambia.domain.model.SolicitacaoAnalise;
import com.dessima.gambia.domain.ports.out.MLClientPort;

public class MLServiceClient implements MLClientPort {

    private final MLServiceHttpClient httpClient;

    public MLServiceClient(MLServiceHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public ResultadoAnalise classificar(SolicitacaoAnalise solicitacao) {
        try {
            return httpClient.chamarML(solicitacao);
        } catch (Exception e) {
            return fallbackDeterministico(solicitacao);
        }
    }

    private ResultadoAnalise fallbackDeterministico(SolicitacaoAnalise s) {
        ClassificacaoEficiencia categoria;
        if (s.consumoKwh() > 400 && s.horasAltoConsumo() > 6 && s.usoHorarioPico()) {
            categoria = ClassificacaoEficiencia.Ineficiente;
        } else if (s.consumoKwh() > 200 || s.horasAltoConsumo() > 4) {
            categoria = ClassificacaoEficiencia.Moderado;
        } else {
            categoria = ClassificacaoEficiencia.Eficiente;
        }

        return new ResultadoAnalise(
                categoria,
                BigDecimal.valueOf(0.75),
                List.of(
                        "Reduzir o uso de equipamentos durante horários de pico",
                        "Avaliar aparelhos com alto consumo energético",
                        "Distribuir atividades de maior consumo ao longo do dia"),
                BigDecimal.ZERO,
                BigDecimal.ZERO);
    }
}
