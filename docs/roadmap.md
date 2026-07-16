# Roadmap

## Legenda

- [x] Implementado e funcional
- [~] Parcialmente implementado / placeholder
- [ ] Não iniciado

## Fase 1 - Protótipo Funcional (Completa)

### Backend

- [x] Arquitetura Hexagonal (Ports & Adapters)
- [x] POST /analise-energetica com Bean Validation
- [x] GlobalExceptionHandler com formato padrão de erro
- [x] JWT (geração, validação, filtro)
- [x] CSRF Double Submit Cookie
- [x] CORS configurado
- [x] Flyway migration V001 (3 tabelas)
- [x] Persistência completa (JPA + PostgreSQL)
- [x] Cliente HTTP para ML Service
- [x] Fallback determinístico no backend (se ML falhar)
- [x] Cálculo de custo (R$ 0,75/kWh)
- [x] Cálculo de emissão CO2 (0,0385 kg/kWh)
- [x] GET /login (dev JWT)
- [x] Configuração via application.yml + env vars
- [x] Actuator health/metrics/prometheus

### ML Service

- [x] FastAPI com lifespan (carregamento único)
- [x] POST /classificar
- [x] GET /health
- [x] Classificador Random Forest (treinamento sintético)
- [x] Cadeia de fallback: ML -> LLM -> Heurística
- [x] Limiar de confiança (0.80) para pular LLM
- [x] Groq API (llama-3.3-70b-versatile)
- [x] Recomendações via LLM com parse estruturado
- [x] Heurística determinística (último fallback)
- [x] CORS liberado
- [x] Pydantic models

### Frontend

- [x] Landing page (Hero + Demo + Features + Footer)
- [x] Tema claro/escuro "Energia Verde"
- [x] Logo SVG (árvore + letra G)
- [x] DemoTool funcional (POST público)
- [x] Exibição de resultado com badge, stats, recomendações
- [x] Bento grid de features
- [x] Glassmorphism no navbar
- [x] Gradientes animados no hero
- [x] Responsivo (mobile-first)
- [x] Toggle de tema com localStorage
- [x] Detecta prefers-color-scheme
- [x] Rotas: /, /login, /cadastrar
- [x] Serviço demo.ts (sem auth)
- [x] Serviço api.ts (com CSRF + JWT)

### Páginas de Autenticação

- [~] Página Login com formulário disabled e banner "Em Breve"
- [~] Página Cadastrar com formulário disabled e banner "Em Breve"

### Infraestrutura

- [x] Docker Compose com 7 serviços
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

## Fase 2 - Autenticação e Usuários (Completa)

### Backend

- [x] Modelo de usuário (tb_usuario)
- [x] Cadastro de usuário (POST /auth/cadastrar)
- [x] Login com credenciais (POST /auth/login)
- [x] Hash de senha (BCrypt)
- [x] Sessão persistente com JWT (cookie httpOnly)
- [x] Vincular imóveis a usuários autenticados (usuario_id opcional)
- [x] Retorno de dados do usuário no login (id, nome, email)

### Frontend

- [x] Formulário de login funcional
- [x] Formulário de cadastro funcional
- [x] AuthContext com persistência em localStorage
- [x] Restauração de sessão ao recarregar
- [x] Navbar com nome real do usuário
- [x] Links condicionais (Login/Cadastrar apenas quando deslogado)
- [x] ScrollToTop button
- [x] PrivateRoute para rotas protegidas
- [x] Login retorna id, nome, email (não mais vazio)

### Testes

- [x] Testes unitários backend (JUnit 5, Mockito, MockMvc, DataJpaTest) — 31 testes
- [x] Testes ML service (pytest, pytest-asyncio) — 35 testes
- [x] H2 para testes de persistência
- [x] application-test.yml com Flyway desabilitado
- [x] Dockerfile backend executa `mvn verify` (Spotless + testes)
- [x] Dockerfile ML service executa `pytest` em multi-stage builder

### Infraestrutura

- [x] Builds executam testes automaticamente via Docker
- [x] .gitignore backend/bin/
- [x] docker-compose.yml sem version (obsoleto)
- [x] PR #4 dev -> main (merged)

## Fase 3 - Histórico e Dashboard

### Backend

- [x] Repositories: findByUsuarioId, findByImovelIdIn, findByAnaliseId
- [x] PersistenciaPort: buscarAnalisesPorUsuario, buscarAnalisePorId
- [x] HistoricoUseCase + HistoricoService
- [x] GET /analises (lista análises do usuário autenticado)
- [x] GET /analises/{id} (detalhe de uma análise)
- [x] GET /dashboard (stats agregadas: total, média, custo, CO2, consumo por mês)
- [x] DashboardResponse DTO com ConsumoMensal

### Frontend

- [x] Página Dashboard com cards de resumo (total análises, média consumo, custo total, CO2 total)
- [x] Gráfico de barras (consumo por mês) via recharts
- [x] Página Histórico com lista de análises por data
- [x] PrivateRoute ativo para /dashboard e /historico
- [x] Navbar com links Dashboard/Histórico quando autenticado
- [x] Novos tipos: AnaliseHistorico, DashboardData, ConsumoMensal
- [x] api.ts: listarAnalises(), buscarDashboard()

### Pendente

- [ ] Exportar relatório (PDF)
- [ ] Comparação entre períodos
- [ ] Metas de economia personalizadas

## Fase 4 - ML Avançado

### Pendente

- [ ] Modelo ONNX (classifier.onnx) serializado
- [ ] Dados reais de treinamento (substituir sintéticos)
- [ ] Feature engineering avançada
- [ ] A/B testing entre modelos
- [ ] Explicabilidade (SHAP/LIME)
- [ ] Pipeline de re-treinamento automático
- [ ] Model registry

## Fase 5 - Produção e Escalabilidade

### Pendente

- [ ] Deploy em nuvem (OCI/AWS)
- [ ] Load balancing
- [ ] Rate limiting
- [ ] WAF e proteção DDoS
- [ ] Backup automatizado do banco
- [ ] Logs centralizados (ELK ou Loki)
- [ ] Alertas (Grafana Alerts)
- [ ] Testes de carga (k6)
- [ ] Testes unitários e de integração

## Fase 6 - Gamificação e Engajamento

### Pendente

- [ ] Selos de eficiência (bronze, prata, ouro)
- [ ] Ranking entre usuários
- [ ] Dicas diárias via notificação
- [ ] Integração com assistentes virtuais
- [ ] Calculadora de economia com base em metas
