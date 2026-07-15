# Seguranca

## Autenticacao (JWT)

O GambIA utiliza JWT (JSON Web Token) armazenado em cookie httpOnly para
autenticacao de usuarios.

### Fluxo

```mermaid
sequenceDiagram
    participant FE as Frontend
    participant BE as Backend

    FE->>BE: GET /login
    BE->>BE: Gera JWT com subject "dev-user"
    BE->>FE: Set-Cookie: SESSION_TOKEN=<jwt>; HttpOnly; Path=/
    Note over FE: Cookie enviado automaticamente em requisicoes subsequentes
    FE->>BE: GET /analise-energetica (com SESSION_TOKEN)
    BE->>BE: JwtAuthenticationFilter extrai token do cookie
    BE->>BE: Valida assinatura + expiracao
    BE->>BE: Seta SecurityContextHolder
    BE-->>FE: Resposta
```

### JwtTokenProvider

- Algoritmo: HS256 (HMAC-SHA256)
- Chave: derivada de `JWT_SECRET_KEY` (minimo 32 caracteres)
- Expiracao: configurada via `JWT_EXPIRATION_MS` (default 24h)
- Claims: `sub` (subject), `iat`, `exp`

### JwtAuthenticationFilter

- Estende `OncePerRequestFilter`
- Le o cookie `SESSION_TOKEN` de cada requisicao
- Se valido, cria `UsernamePasswordAuthenticationToken` com role `ROLE_USER`
- Nao bloqueia requisicoes sem token (apenas opcional)

## CSRF (Double Submit Cookie)

Protecao contra Cross-Site Request Forgery via Double Submit Cookie Pattern.

### Configuracao (SecurityConfig.java)

```java
http.csrf(csrf ->
    csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .csrfTokenRequestHandler(csrfHandler)
        .ignoringRequestMatchers("/analise-energetica"))
```

### Fluxo

1. Backend define cookie `XSRF-TOKEN` (nao-httpOnly, legivel por JS)
2. Frontend le o cookie e envia o valor no header `X-XSRF-TOKEN`
3. Backend compara os dois valores

### Excecao

O endpoint `/analise-energetica` e ignorado pelo CSRF (permitAll + sem CSRF)
para permitir o uso da ferramenta de demo publica sem necessidade de
autenticacao.

## CORS

### Configuracao (CorsConfig.java)

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

## Autorizacao

Atualmente toda requisicao autenticada recebe `ROLE_USER`. Nao ha
hierarquia de papeis implementada. O controlador de analise esta
configurado com `.anyRequest().permitAll()` para fins de prototipo.

## Resumo dos Endpoints

| Endpoint | Autenticacao | CSRF | Uso |
|----------|-------------|------|-----|
| `POST /analise-energetica` | Opcional (JWT) | Ignorado | Demo publica |
| `GET /login` | Nao | Protegido | Dev: obter JWT |
| `GET /actuator/**` | Opcional | Protegido | Health checks |
