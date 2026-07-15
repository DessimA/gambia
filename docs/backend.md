# Backend - Spring Boot / Java 21

## Estrutura de Pacotes (Arquitetura Hexagonal)

```
backend/src/main/java/com/dessima/gambia/
|
+-- GambiaApplication.java
|
+-- domain/                          # Núcleo da Aplicação (Core)
|   +-- model/                       # Entidades de Domínio
|   |   +-- SolicitacaoAnalise.java  # Record: dados de entrada da análise
|   |   +-- ResultadoAnalise.java    # Record: resultado consolidado
|   |   +-- ClassificacaoEficiencia.java  # Enum: Eficiente, Moderado, Ineficiente
|   |   +-- TipoImovel.java          # Enum: Casa, Apartamento, Comércio, ...
|   +-- ports/
|   |   +-- in/
|   |   |   +-- ObterAnaliseUseCase.java  # Interface do caso de uso
|   |   +-- out/
|   |       +-- MLClientPort.java         # SPI para chamada ao ML
|   |       +-- PersistenciaPort.java     # SPI para persistência
|   +-- service/
|       +-- AnaliseEnergiaService.java    # Implementação do caso de uso
|
+-- adapters/
    +-- config/
    |   +-- HexagonalConfig.java    # Wiring de beans (DI manual)
    +-- in/                         # Adaptadores de Entrada
    |   +-- dto/
    |   |   +-- AnaliseRequest.java       # Record com Bean Validation
    |   |   +-- AnaliseResponse.java      # Record de resposta
    |   |   +-- ErroResponse.java         # Record de erro padrão
    |   +-- security/
    |   |   +-- CorsConfig.java           # CORS para localhost:5173
    |   |   +-- SecurityConfig.java       # Spring Security + CSRF
    |   |   +-- JwtTokenProvider.java     # Geração/validação de JWT
    |   |   +-- JwtAuthenticationFilter.java  # Filtro de autenticação
    |   +-- web/
    |       +-- AnaliseController.java    # POST /analise-energetica
    |       +-- AuthController.java       # GET /login (dev JWT)
    |       +-- GlobalExceptionHandler.java   # Tratamento de erros
    +-- out/                        # Adaptadores de Saída
        +-- client/
        |   +-- MLServiceClient.java       # Implementação do MLClientPort
        |   +-- MLServiceHttpClient.java   # Cliente HTTP para ML Service
        +-- persistence/
            +-- ImovelEntity.java          # JPA Entity: tb_imovel
            +-- AnaliseConsumoEntity.java  # JPA Entity: tb_analise_consumo
            +-- RecomendacaoGeradaEntity.java  # JPA Entity: tb_recomendacao_gerada
            +-- ImovelRepository.java      # JPA Repository
            +-- AnaliseConsumoRepository.java  # JPA Repository
            +-- RecomendacaoGeradaRepository.java  # JPA Repository
            +-- PersistenciaJpaAdapter.java    # Implementação da PersistênciaPort
```

## Endpoint: POST /analise-energetica

### Request Body

```json
{
  "imovel_id": "uuid-opcional",
  "consumo_kwh": 420,
  "uso_horario_pico": true,
  "quantidade_equipamentos": 10,
  "tipo_imovel": "Casa",
  "horas_alto_consumo": 8
}
```

### Regras de Validação (Bean Validation)

| Campo | Regras |
|-------|--------|
| `consumo_kwh` | @NotNull, @Positive |
| `uso_horario_pico` | @NotNull |
| `quantidade_equipamentos` | @NotNull, @Min(0) |
| `tipo_imovel` | @NotBlank |
| `horas_alto_consumo` | @NotNull, @Min(0), @Max(24) |

### Response (200 OK)

```json
{
  "categoria": "Ineficiente",
  "probabilidade": 0.81,
  "recomendacoes": [
    "Reduzir o uso de equipamentos durante horarios de pico",
    "Avaliar aparelhos com alto consumo energetico",
    "Distribuir atividades de maior consumo ao longo do dia"
  ],
  "custo_estimado_mensal": 315.00
}
```

### Response (400 Bad Request)

```json
{
  "timestamp": "2026-07-15T14:24:00Z",
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Falha na validacao dos campos de entrada",
  "campos": {
    "horas_alto_consumo": "O valor de horas diarias de alto consumo deve estar entre 0 e 24"
  }
}
```

## Serviço de Domínio: AnaliseEnergiaService

Fluxo de execução:

1. Recebe `SolicitacaoAnalise` do controller
2. Chama `MLClientPort.classificar()` que encaminha ao ML Service
3. Calcula custo estimado: `consumo_kwh * R$ 0,75`
4. Calcula emissão CO2: `consumo_kwh * 0,0385 kg/kWh`
5. Persiste dados via `PersistenciaPort`:
    - Salva/atualiza imóvel em `tb_imovel`
    - Salva análise em `tb_analise_consumo`
    - Salva recomendações em `tb_recomendacao_gerada`
6. Retorna `ResultadoAnalise` completo

## Tratamento de Erros

`GlobalExceptionHandler` (RestControllerAdvice) captura:

- `MethodArgumentNotValidException` -> 400 com mapa de campos
- `IllegalArgumentException` -> 400 (ex: tipo_imovel inválido)
- `Exception` -> 500 genérico

## Endpoint: GET /login

Endpoint temporário de dev que gera um JWT e o define como cookie
`SESSION_TOKEN` (httpOnly). Sem autenticação real neste protótipo.
