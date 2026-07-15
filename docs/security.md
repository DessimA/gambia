# Segurança

## Autenticação (JWT)

O GambIA utiliza JWT (JSON Web Token) armazenado em cookie httpOnly para
autenticação de usuários.

### Fluxo

```mermaid
sequenceDiagram
    participant FE as Frontend
    participant BE as Backend

    FE->>BE: GET /login
    BE->>BE: Gera JWT com subject "dev-user"
    BE->>FE: "Set-Cookie: SESSION_TOKEN=&lt;jwt&gt;; HttpOnly; Path=/"
    Note over FE: Cookie enviado automaticamente em requisicoes subsequentes
    FE->>BE: GET /analise-energetica (com SESSION_TOKEN)
    BE->>BE: JwtAuthenticationFilter extrai token do cookie
    BE->>BE: Valida assinatura + expiracao
    BE->>BE: Seta SecurityContextHolder
    BE-->>FE: Resposta
```

### JwtTokenProvider

- Algoritmo: HS256 (HMAC-SHA256)
- Chave: derivada de `JWT_SECRET_KEY` (mínimo 32 caracteres)
- Expiração: configurada via `JWT_EXPIRATION_MS` (default 24h)
- Claims: `sub` (subject), `iat`, `exp`

### JwtAuthenticationFilter

- Estende `OncePerRequestFilter`
- Lê o cookie `SESSION_TOKEN` de cada requisição
- Se válido, cria `UsernamePasswordAuthenticationToken` com role `ROLE_USER`
- Não bloqueia requisições sem token (apenas opcional)

## CSRF (Double Submit Cookie)

Proteção contra Cross-Site Request Forgery via Double Submit Cookie Pattern.

### Configuração (SecurityConfig.java)

```java
http.csrf(csrf ->
    csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .csrfTokenRequestHandler(csrfHandler)
        .ignoringRequestMatchers("/analise-energetica"))
```

### Fluxo

1. Backend define cookie `XSRF-TOKEN` (não-httpOnly, legível por JS)
2. Frontend lê o cookie e envia o valor no header `X-XSRF-TOKEN`
3. Backend compara os dois valores

### Exceção

O endpoint `/analise-energetica` é ignorado pelo CSRF (permitAll + sem CSRF)
para permitir o uso da ferramenta de demo pública sem necessidade de
autenticação.

## CORS

### Configuração (CorsConfig.java)

```java
registry.addMapping("/**")
    .allowedOrigins("http://localhost:5173", "http://localhost:3000")
    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    .allowCredentials(true)
    .allowedHeaders("*");
```

Origens permitidas:

- `http://localhost:5173` (frontend em desenvolvimento)
- `http://localhost:3000` (Grafana)

## Autorização

Atualmente toda requisição autenticada recebe `ROLE_USER`. Não há
hierarquia de papéis implementada. O controlador de análise está
configurado com `.anyRequest().permitAll()` para fins de protótipo.

## Resumo dos Endpoints

| Endpoint | Autenticação | CSRF | Uso |
|----------|-------------|------|-----|
| `POST /analise-energetica` | Opcional (JWT) | Ignorado | Demo pública |
| `GET /login` | Não | Protegido | Dev: obter JWT |
| `GET /actuator/**` | Opcional | Protegido | Health checks |
