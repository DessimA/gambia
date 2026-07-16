package com.dessima.gambia.adapters.config;

import com.dessima.gambia.adapters.out.client.MLServiceClient;
import com.dessima.gambia.adapters.out.client.MLServiceHttpClient;
import com.dessima.gambia.domain.ports.in.AutenticacaoUseCase;
import com.dessima.gambia.domain.ports.out.MLClientPort;
import com.dessima.gambia.domain.ports.out.PersistenciaPort;
import com.dessima.gambia.domain.ports.out.UsuarioRepositoryPort;
import com.dessima.gambia.domain.service.AnaliseEnergiaService;
import com.dessima.gambia.domain.service.AutenticacaoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

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
  public AutenticacaoUseCase autenticacaoUseCase(
      UsuarioRepositoryPort usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
    return new AutenticacaoService(usuarioRepository, passwordEncoder);
  }

  @Bean
  public AnaliseEnergiaService analiseEnergiaService(
      MLClientPort mlClient, PersistenciaPort persistencia) {
    return new AnaliseEnergiaService(mlClient, persistencia);
  }
}
