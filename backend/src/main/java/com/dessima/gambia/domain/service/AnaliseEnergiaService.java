package com.dessima.gambia.domain.service;

import com.dessima.gambia.domain.model.ResultadoAnalise;
import com.dessima.gambia.domain.model.SolicitacaoAnalise;
import com.dessima.gambia.domain.ports.in.ObterAnaliseUseCase;
import com.dessima.gambia.domain.ports.out.MLClientPort;
import com.dessima.gambia.domain.ports.out.PersistenciaPort;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class AnaliseEnergiaService implements ObterAnaliseUseCase {

  private static final BigDecimal TARIFA_KWH = new BigDecimal("0.75");
  private static final BigDecimal FATOR_CO2 = new BigDecimal("0.0385");

  private final MLClientPort mlClient;
  private final PersistenciaPort persistencia;

  public AnaliseEnergiaService(MLClientPort mlClient, PersistenciaPort persistencia) {
    this.mlClient = mlClient;
    this.persistencia = persistencia;
  }

  @Override
  public ResultadoAnalise executar(SolicitacaoAnalise solicitacao) {
    ResultadoAnalise resultado = mlClient.classificar(solicitacao);

    BigDecimal custo = calcularCusto(solicitacao.consumoKwh());
    BigDecimal co2 = calcularCo2(solicitacao.consumoKwh());

    ResultadoAnalise completo =
        new ResultadoAnalise(
            resultado.categoria(),
            resultado.probabilidade(),
            resultado.recomendacoes(),
            custo,
            co2);

    var imovelId = persistencia.salvarImovel(solicitacao);
    var analiseId = persistencia.salvarAnalise(imovelId, solicitacao, completo);
    persistencia.salvarRecomendacoes(analiseId, completo);

    return completo;
  }

  public static BigDecimal calcularCusto(double consumoKwh) {
    return BigDecimal.valueOf(consumoKwh).multiply(TARIFA_KWH).setScale(2, RoundingMode.HALF_UP);
  }

  public static BigDecimal calcularCo2(double consumoKwh) {
    return BigDecimal.valueOf(consumoKwh).multiply(FATOR_CO2).setScale(3, RoundingMode.HALF_UP);
  }
}
