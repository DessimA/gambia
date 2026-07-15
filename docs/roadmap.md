# Roadmap

## Legenda

- [x] Implementado e funcional
- [~] Parcialmente implementado / placeholder
- [ ] Nao iniciado

## Fase 1 - Prototipo Funcional (Completa)

### Backend

- [x] Arquitetura Hexagonal (Ports & Adapters)
- [x] POST /analise-energetica com Bean Validation
- [x] GlobalExceptionHandler com formato padrao de erro
- [x] JWT (geracao, validacao, filtro)
- [x] CSRF Double Submit Cookie
- [x] CORS configurado
- [x] Flyway migration V001 (3 tabelas)
- [x] Persistencia completa (JPA + PostgreSQL)
- [x] Cliente HTTP para ML Service
- [x] Fallback deterministico no backend (se ML falhar)
- [x] Calculo de custo (R$ 0,75/kWh)
- [x] Calculo de emissao CO2 (0,0385 kg/kWh)
- [x] GET /login (dev JWT)
- [x] Configuracao via application.yml + env vars
- [x] Actuator health/metrics/prometheus

### ML Service

- [x] FastAPI com lifespan (carregamento unico)
- [x] POST /classificar
- [x] GET /health
- [x] Classificador Random Forest (treinamento sintetico)
- [x] Cadeia de fallback: ML -> LLM -> Heuristica
- [x] Limiar de confianca (0.80) para pular LLM
- [x] Groq API (llama-3.3-70b-versatile)
- [x] Recomendacoes via LLM com parse estruturado
- [x] Heuristica deterministica (ultimo fallback)
- [x] CORS liberado
- [x] Pydantic models

### Frontend

- [x] Landing page (Hero + Demo + Features + Footer)
- [x] Tema claro/escuro "Energia Verde"
- [x] Logo SVG (arvore + letra G)
- [x] DemoTool funcional (POST publico)
- [x] Exibicao de resultado com badge, stats, recomendacoes
- [x] Bento grid de features
- [x] Glassmorphism no navbar
- [x] Gradientes animados no hero
- [x] Responsivo (mobile-first)
- [x] Toggle de tema com localStorage
- [x] Detecta prefers-color-scheme
- [x] Rotas: /, /login, /cadastrar
- [x] Servico demo.ts (sem auth)
- [x] Servico api.ts (com CSRF + JWT)

### Paginas de Autenticacao

- [~] Pagina Login com formulario disabled e banner "Em Breve"
- [~] Pagina Cadastrar com formulario disabled e banner "Em Breve"

### Infraestrutura

- [x] Docker Compose com 7 servicos
- [x] Dockerfile para backend (multi-stage: Maven build + JRE)
- [x] Dockerfile para ML service (Python slim)
- [x] Dockerfile para frontend (Node build + serve)
- [x] OpenTelemetry Collector
- [x] Prometheus
- [x] Grafana com provisioning
- [x] Rede interna (gambia-net)
- [x] Volumes persistentes (pgdata, promdata, grafanadata)
- [x] Healthcheck no PostgreSQL
- [x] .env.example

### CI/CD

- [x] GitHub Actions: lint (3 jobs paralelos)
- [x] Backend: Maven spotless:check + compile
- [x] ML Service: Ruff check
- [x] Frontend: tsc --noEmit
- [x] PR #1 dev -> main (merged)

## Fase 2 - Autenticacao e Usuarios

### Pendente

- [ ] Modelo de usuario (tb_usuario)
- [ ] Cadastro de usuario (POST /auth/cadastrar)
- [ ] Login com credenciais (POST /auth/login)
- [ ] Hash de senha (BCrypt)
- [ ] Sessao persistente com JWT
- [ ] Vincular imoveis a usuarios autenticados
- [ ] Formularios de login/cadastro funcionais no frontend
- [ ] Rotas protegidas no frontend (PrivateRoute)
- [ ] Refresh token

## Fase 3 - Historico e Dashboard

### Pendente

- [ ] Pagina de historico de analises por usuario
- [ ] Dashboard com graficos de consumo ao longo do tempo
- [ ] Exportar relatorio (PDF)
- [ ] Comparacao entre periodos
- [ ] Metas de economia personalizadas

## Fase 4 - ML Avancado

### Pendente

- [ ] Modelo ONNX (classifier.onnx) serializado
- [ ] Dados reais de treinamento (substituir sinteticos)
- [ ] Feature engineering avancada
- [ ] A/B testing entre modelos
- [ ] Explicabilidade (SHAP/LIME)
- [ ] Pipeline de re-treinamento automatico
- [ ] Model registry

## Fase 5 - Producao e Escalabilidade

### Pendente

- [ ] Deploy em nuvem (OCI/AWS)
- [ ] Load balancing
- [ ] Rate limiting
- [ ] WAF e protecao DDoS
- [ ] Backup automatizado do banco
- [ ] Logs centralizados (ELK ou Loki)
- [ ] Alertas (Grafana Alerts)
- [ ] Testes de carga (k6)
- [ ] Testes unitarios e de integracao

## Fase 6 - Gamificacao e Engajamento

### Pendente

- [ ] Selos de eficiencia (bronze, prata, ouro)
- [ ] Ranking entre usuarios
- [ ] Dicas diarias via notificacao
- [ ] Integracao com assistentes virtuais
- [ ] Calculadora de economia com base em metas
