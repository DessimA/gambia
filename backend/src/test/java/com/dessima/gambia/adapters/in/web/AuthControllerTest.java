package com.dessima.gambia.adapters.in.web;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dessima.gambia.adapters.in.security.JwtTokenProvider;
import com.dessima.gambia.domain.model.Usuario;
import com.dessima.gambia.domain.ports.in.AutenticacaoUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@WithMockUser
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private AutenticacaoUseCase autenticacaoUseCase;
  @MockBean private JwtTokenProvider tokenProvider;

  @Test
  void deveCadastrarComSucesso() throws Exception {
    when(autenticacaoUseCase.cadastrar("Teste", "test@test.com", "senha123"))
        .thenReturn(
            new Usuario(UUID.randomUUID(), "Teste", "test@test.com", "hash", Instant.now()));

    var request = Map.of("nome", "Teste", "email", "test@test.com", "senha", "senha123");

    mockMvc
        .perform(
            post("/auth/cadastrar")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());
  }

  @Test
  void deveRetornar400_QuandoCadastroComEmailInvalido() throws Exception {
    var request = Map.of("nome", "Teste", "email", "invalido", "senha", "senha123");

    mockMvc
        .perform(
            post("/auth/cadastrar")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deveFazerLoginComSucesso() throws Exception {
    var usuario = new Usuario(UUID.randomUUID(), "Teste", "test@test.com", "hash", Instant.now());
    when(autenticacaoUseCase.login("test@test.com", "senha123")).thenReturn(usuario);
    when(tokenProvider.gerarToken(usuario.id().toString())).thenReturn("token_jwt");

    var request = Map.of("email", "test@test.com", "senha", "senha123");

    mockMvc
        .perform(
            post("/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(cookie().exists("SESSION_TOKEN"))
        .andExpect(cookie().httpOnly("SESSION_TOKEN", true))
        .andExpect(cookie().path("SESSION_TOKEN", "/"))
        .andExpect(cookie().maxAge("SESSION_TOKEN", 86400));
  }

  @Test
  void deveRetornar400_QuandoLoginComSenhaInvalida() throws Exception {
    when(autenticacaoUseCase.login("test@test.com", "senha_errada"))
        .thenThrow(new IllegalArgumentException("E-mail ou senha invalidos"));

    var request = Map.of("email", "test@test.com", "senha", "senha_errada");

    mockMvc
        .perform(
            post("/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.mensagem").value("E-mail ou senha invalidos"));
  }

  @Test
  void deveRetornar400_QuandoLoginComCamposAusentes() throws Exception {
    mockMvc
        .perform(
            post("/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest());
  }
}
