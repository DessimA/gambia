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
|   |   +-- Usuario.java             # Record: id, nome, email, senhaHash
|   +-- ports/
|   |   +-- in/
|   |   |   +-- ObterAnaliseUseCase.java      # Interface do caso de uso
|   |   |   +-- AutenticacaoUseCase.java      # Cadastro/login
|   |   +-- out/
|   |       +-- MLClientPort.java             # SPI para chamada ao ML
|   |       +-- PersistenciaPort.java         # SPI para persistência
|   |       +-- UsuarioRepositoryPort.java    # SPI para usuários
|   +-- service/
|       +-- AnaliseEnergiaService.java        # Implementação do caso de uso
|       +-- AutenticacaoService.java          # Cadastro/login com BCrypt
|
+-- adapters/
    +-- config/
    |   +-- HexagonalConfig.java    # Wiring de beans (DI manual)
    +-- in/                         # Adaptadores de Entrada
    |   +-- dto/
    |   |   +-- AnaliseRequest.java       # Record com Bean Validation
    |   |   +-- AnaliseResponse.java      # Record de resposta
    |   |   +-- ErroResponse.java         # Record de erro padrão
    |   |   +-- CadastroRequest.java      # nome, email, senha
    |   |   +-- LoginRequest.java         # email, senha
    |   |   +-- LoginResponse.java        # id, nome, email
    |   +-- security/
    |   |   +-- CorsConfig.java           # CORS para localhost:5173
    |   |   +-- SecurityConfig.java       # Spring Security + CSRF
    |   |   +-- JwtTokenProvider.java     # Geração/validação de JWT
    |   |   +-- JwtAuthenticationFilter.java  # Filtro de autenticação
    |   +-- web/
    |       +-- AnaliseController.java    # POST /analise-energetica
    |       +-- AuthController.java       # POST /auth/cadastrar, POST /auth/login
    |       +-- GlobalExceptionHandler.java   # Tratamento de erros
    +-- out/                        # Adaptadores de Saída
        +-- client/
        |   +-- MLServiceClient.java       # Implementação do MLClientPort
        |   +-- MLServiceHttpClient.java   # Cliente HTTP para ML Service
        +-- persistence/
            +-- ImovelEntity.java          # JPA Entity: tb_imovel
            +-- AnaliseConsumoEntity.java  # JPA Entity: tb_analise_consumo
            +-- RecomendacaoGeradaEntity.java  # JPA Entity: tb_recomendacao_gerada
            +-- UsuarioEntity.java         # JPA Entity: tb_usuario
            +-- ImovelRepository.java      # JPA Repository
            +-- AnaliseConsumoRepository.java  # JPA Repository
            +-- RecomendacaoGeradaRepository.java  # JPA Repository
            +-- UsuarioRepository.java     # JPA Repository (findByEmail)
            +-- PersistenciaJpaAdapter.java    # Implementação da PersistênciaPort
            +-- UsuarioJpaAdapter.java     # Implementação do UsuarioRepositoryPort
```

## Endpoints

### POST /analise-energetica

Público (sem autenticação necessária). Aceita `imovel_id` opcional para associar a um imóvel existente.

**Request Body**
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

**Regras de Validação (Bean Validation)**

| Campo | Regras |
|-------|--------|
| `consumo_kwh` | @NotNull, @Positive |
| `uso_horario_pico` | @NotNull |
| `quantidade_equipamentos` | @NotNull, @Min(0) |
| `tipo_imovel` | @NotBlank |
| `horas_alto_consumo` | @NotNull, @Min(0), @Max(24) |

**Response (200 OK)**
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

**Response (400 Bad Request)**
```json
{
  "timestamp": "2026-07-15T14:24:00Z",
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Falha na validação dos campos de entrada",
  "campos": {
    "horas_alto_consumo": "O valor de horas diárias de alto consumo deve estar entre 0 e 24"
  }
}
```

### POST /auth/cadastrar

Cria um novo usuário. Retorna cookie `SESSION_TOKEN` e dados do usuário.

**Request Body**
```json
{
  "nome": "Maria Silva",
  "email": "maria@email.com",
  "senha": "minhaSenha123"
}
```

**Validações**: nome (obrigatório), email (formato válido), senha (mínimo 6 caracteres).

**Response (201 Created)**: Corpo vazio. O cookie `SESSION_TOKEN` é definido na resposta.
O frontend chama login em seguida para obter `{ id, nome, email }`.

Erro: `400 Bad Request` se email já cadastrado.

### POST /auth/login

Autentica usuário existente. Retorna cookie `SESSION_TOKEN` e dados do usuário.

**Request Body**
```json
{
  "email": "maria@email.com",
  "senha": "minhaSenha123"
}
```

**Response (200 OK)**
```json
{
  "id": "uuid",
  "nome": "Maria Silva",
  "email": "maria@email.com"
}
```

Erro: `400 Bad Request` se credenciais inválidas.

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

## Serviço de Domínio: AutenticacaoService

Fluxo de cadastro:
1. Valida senha (mínimo 6 caracteres)
2. Verifica se email já existe (lança exceção se duplicado)
3. Hash da senha com BCrypt
4. Persiste `Usuario` via `UsuarioRepositoryPort`
5. Gera JWT e retorna dados do usuário

Fluxo de login:
1. Busca usuário por email
2. Verifica senha com BCrypt
3. Gera JWT e retorna dados do usuário

## Tratamento de Erros

`GlobalExceptionHandler` (RestControllerAdvice) captura:

- `MethodArgumentNotValidException` -> 400 com mapa de campos
- `IllegalArgumentException` -> 400 (ex: tipo_imovel inválido)
- `IllegalArgumentException` com "E-mail ja cadastrado" -> 400 Bad Request
- `IllegalArgumentException` com "E-mail ou senha invalidos" -> 400 Bad Request
- `Exception` -> 500 genérico

## Testes (JUnit 5 + Mockito + MockMvc + DataJpaTest)

31 testes automatizados no backend (JVM):

| Classe | Qtde | Escopo |
|--------|------|--------|
| `AnaliseEnergiaServiceTest` | 6 | Cálculo custo/CO2, execução completa |
| `AutenticacaoServiceTest` | 5 | Cadastro/login, duplicidade, senha inválida |
| `JwtTokenProviderTest` | 6 | Geração, validação, extração subject |
| `AnaliseControllerTest` | 5 | POST /analise-energetica, validações |
| `AuthControllerTest` | 5 | POST /auth/cadastrar, POST /auth/login |
| `UsuarioRepositoryTest` | 4 | Save, findByEmail, unicidade |

+ 39 testes no ML Service (pytest), totalizando 70 testes automatizados.

Configuração de teste em `src/test/resources/application-test.yml` com H2 em modo PostgreSQL. Flyway desabilitado em testes (JPA `ddl-auto: create-drop`).
