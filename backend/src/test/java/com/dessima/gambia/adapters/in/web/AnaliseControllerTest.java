package com.dessima.gambia.adapters.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dessima.gambia.domain.model.ClassificacaoEficiencia;
import com.dessima.gambia.domain.model.ResultadoAnalise;
import com.dessima.gambia.domain.ports.in.ObterAnaliseUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AnaliseController.class)
@WithMockUser
class AnaliseControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private ObterAnaliseUseCase useCase;

  @Test
  void deveAnalisarComSucesso() throws Exception {
    when(useCase.executar(any()))
        .thenReturn(
            new ResultadoAnalise(
                ClassificacaoEficiencia.Moderado,
                new BigDecimal("0.95"),
                List.of("Reduzir consumo"),
                new BigDecimal("225.00"),
                new BigDecimal("11.550")));

    var request =
        Map.of(
            "consumo_kwh", 300,
            "tipo_imovel", "Casa",
            "quantidade_equipamentos", 10,
            "uso_horario_pico", true,
            "horas_alto_consumo", 5);

    mockMvc
        .perform(
            post("/analise-energetica")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.categoria").value("Moderado"))
        .andExpect(jsonPath("$.probabilidade").value(0.95))
        .andExpect(jsonPath("$.custo_estimado_mensal").value(225.00))
        .andExpect(jsonPath("$.recomendacoes[0]").value("Reduzir consumo"));
  }

  @Test
  void deveRetornar400_QuandoConsumoNegativo() throws Exception {
    var request =
        Map.of(
            "consumo_kwh", -1,
            "tipo_imovel", "Casa",
            "quantidade_equipamentos", 10,
            "uso_horario_pico", true,
            "horas_alto_consumo", 5);

    mockMvc
        .perform(
            post("/analise-energetica")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deveRetornar400_QuandoHorasExcedem24() throws Exception {
    var request =
        Map.of(
            "consumo_kwh", 300,
            "tipo_imovel", "Casa",
            "quantidade_equipamentos", 10,
            "uso_horario_pico", true,
            "horas_alto_consumo", 25);

    mockMvc
        .perform(
            post("/analise-energetica")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deveRetornar400_QuandoTipoImovelInvalido() throws Exception {
    var request =
        Map.of(
            "consumo_kwh", 300,
            "tipo_imovel", "Invalido",
            "quantidade_equipamentos", 10,
            "uso_horario_pico", true,
            "horas_alto_consumo", 5);

    mockMvc
        .perform(
            post("/analise-energetica")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.mensagem").value("Tipo de im\u00f3vel inv\u00e1lido"));
  }

  @Test
  void deveRetornar400_QuandoCamposObrigatoriosAusentes() throws Exception {
    mockMvc
        .perform(
            post("/analise-energetica")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isBadRequest());
  }
}
