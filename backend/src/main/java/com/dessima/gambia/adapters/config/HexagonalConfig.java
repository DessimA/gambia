package com.dessima.gambia.adapters.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.dessima.gambia.adapters.out.client.MLServiceClient;
import com.dessima.gambia.adapters.out.client.MLServiceHttpClient;
import com.dessima.gambia.domain.ports.out.MLClientPort;
import com.dessima.gambia.domain.ports.out.PersistenciaPort;
import com.dessima.gambia.domain.service.AnaliseEnergiaService;

@Configuration
public class HexagonalConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public MLClientPort mlClientPort(MLServiceHttpClient httpClient) {
        return new MLServiceClient(httpClient);
    }

    @Bean
    public AnaliseEnergiaService analiseEnergiaService(MLClientPort mlClient, PersistenciaPort persistencia) {
        return new AnaliseEnergiaService(mlClient, persistencia);
    }
}
