package com.dessima.gambia.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.dessima.gambia.domain.model.ClassificacaoEficiencia;
import com.dessima.gambia.domain.model.ResultadoAnalise;
import com.dessima.gambia.domain.model.SolicitacaoAnalise;
import com.dessima.gambia.domain.model.TipoImovel;
import com.dessima.gambia.domain.ports.out.MLClientPort;
import com.dessima.gambia.domain.ports.out.PersistenciaPort;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnaliseEnergiaServiceTest {

  @Mock private MLClientPort mlClient;
  @Mock private PersistenciaPort persistencia;

  private AnaliseEnergiaService service;

  @BeforeEach
  void setUp() {
    service = new AnaliseEnergiaService(mlClient, persistencia);
  }

  @Test
  void deveCalcularCusto() {
    BigDecimal custo = AnaliseEnergiaService.calcularCusto(300);
    assertEquals(new BigDecimal("225.00"), custo);
  }

  @Test
  void deveCalcularCustoZero() {
    BigDecimal custo = AnaliseEnergiaService.calcularCusto(0);
    assertEquals(new BigDecimal("0.00"), custo);
  }

  @Test
  void deveCalcularCo2() {
    BigDecimal co2 = AnaliseEnergiaService.calcularCo2(300);
    assertEquals(new BigDecimal("11.550"), co2);
  }

  @Test
  void deveCalcularCo2Zero() {
    BigDecimal co2 = AnaliseEnergiaService.calcularCo2(0);
    assertEquals(new BigDecimal("0.000"), co2);
  }

  @Test
  void deveExecutarAnaliseCompleta() {
    var solicitacao =
        new SolicitacaoAnalise(
            Optional.empty(), 300, true, 10, TipoImovel.Casa, 5, Optional.empty());
    var resultadoML =
        new ResultadoAnalise(
            ClassificacaoEficiencia.Moderado,
            new BigDecimal("0.95"),
            List.of("Reduzir consumo"),
            BigDecimal.ZERO,
            BigDecimal.ZERO);

    when(mlClient.classificar(solicitacao)).thenReturn(resultadoML);
    when(persistencia.salvarImovel(solicitacao)).thenReturn(UUID.randomUUID());
    when(persistencia.salvarAnalise(any(), eq(solicitacao), any())).thenReturn(UUID.randomUUID());

    ResultadoAnalise resultado = service.executar(solicitacao);

    assertEquals(ClassificacaoEficiencia.Moderado, resultado.categoria());
    assertEquals(new BigDecimal("225.00"), resultado.custoEstimadoMensal());
    assertEquals(new BigDecimal("11.550"), resultado.emissaoCo2Kg());
    assertEquals(List.of("Reduzir consumo"), resultado.recomendacoes());
    verify(mlClient).classificar(solicitacao);
    verify(persistencia).salvarImovel(solicitacao);
    verify(persistencia).salvarAnalise(any(), eq(solicitacao), any());
    verify(persistencia).salvarRecomendacoes(any(), any());
  }

  @Test
  void deveUsarConsumoParaCalcularCustoECo2() {
    var solicitacao =
        new SolicitacaoAnalise(
            Optional.empty(), 500, false, 15, TipoImovel.Industria, 8, Optional.empty());
    var resultadoML =
        new ResultadoAnalise(
            ClassificacaoEficiencia.Ineficiente,
            new BigDecimal("0.99"),
            List.of("Trocar equipamentos"),
            BigDecimal.ZERO,
            BigDecimal.ZERO);

    when(mlClient.classificar(solicitacao)).thenReturn(resultadoML);
    when(persistencia.salvarImovel(solicitacao)).thenReturn(UUID.randomUUID());
    when(persistencia.salvarAnalise(any(), eq(solicitacao), any())).thenReturn(UUID.randomUUID());

    ResultadoAnalise resultado = service.executar(solicitacao);

    assertEquals(new BigDecimal("375.00"), resultado.custoEstimadoMensal());
    assertEquals(new BigDecimal("19.250"), resultado.emissaoCo2Kg());
  }
}
