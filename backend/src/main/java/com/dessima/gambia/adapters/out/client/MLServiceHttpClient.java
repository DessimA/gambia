package com.dessima.gambia.adapters.out.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dessima.gambia.domain.model.ClassificacaoEficiencia;
import com.dessima.gambia.domain.model.ResultadoAnalise;
import com.dessima.gambia.domain.model.SolicitacaoAnalise;

@Component
public class MLServiceHttpClient {

    private final RestTemplate restTemplate;
    private final String mlServiceUrl;

    public MLServiceHttpClient(RestTemplate restTemplate,
                                @Value("${app.ml.service-url}") String mlServiceUrl) {
        this.restTemplate = restTemplate;
        this.mlServiceUrl = mlServiceUrl;
    }

    @SuppressWarnings("unchecked")
    public ResultadoAnalise chamarML(SolicitacaoAnalise solicitacao) {
        var request = Map.of(
                "consumo_kwh", solicitacao.consumoKwh(),
                "uso_horario_pico", solicitacao.usoHorarioPico(),
                "quantidade_equipamentos", solicitacao.quantidadeEquipamentos(),
                "tipo_imovel", solicitacao.tipoImovel().name(),
                "horas_alto_consumo", solicitacao.horasAltoConsumo());

        var response = restTemplate.postForEntity(
                mlServiceUrl + "/classificar",
                request,
                Map.class);

        Map<String, Object> body = response.getBody();
        if (body == null) {
            throw new RuntimeException("Resposta vazia do ML service");
        }

        return new ResultadoAnalise(
                ClassificacaoEficiencia.valueOf((String) body.get("categoria")),
                new BigDecimal(body.get("probabilidade").toString()),
                (List<String>) body.get("recomendacoes"),
                BigDecimal.ZERO,
                BigDecimal.ZERO);
    }
}
