# Guia de Contribuicao

Obrigado por considerar contribuir com o GambIA! Este documento orienta como
participar do desenvolvimento do projeto.

## Codigo de Conduta

Ao participar deste projeto, voce se compromete a manter um ambiente
respeitoso e colaborativo. Naoo toleramos assedio ou discriminacao de
qualquer natureza.

## Como Contribuir

### Reportar Problemas

1. Verifique se o problema ja nao foi reportado nas
   [issues](https://github.com/DessimA/gambia/issues)
2. Abra uma nova issue com:
   - Titulo descritivo
   - Passos para reproduzir
   - Comportamento esperado vs observado
   - Logs ou screenshots, se aplicavel
   - Ambiente (SO, versao do Docker, navegador)

### Sugerir Melhorias

Abra uma issue com o rotulo `enhancement` descrevendo:

- O problema que a melhoria resolve
- Como voce imagina a solucao
- Exemplos de implementacao similar (se houver)

### Submeter Alteracoes (Pull Requests)

1. Faca um fork do repositorio
2. Crie uma branch a partir de `dev`:
   ```bash
   git checkout dev
   git checkout -b feat/minha-melhoria
   ```
3. Siga as convencoes do projeto (veja abaixo)
4. Commit com mensagens claras em ingles ou portugues:
   ```bash
   git commit -m "feat: adiciona calculo de economia anual"
   ```
5. Envie para seu fork e abra um Pull Request para a branch `dev`
6. Aguarde a revisao e a execucao do CI (lint)

## Convencoes do Projeto

### Codigo

- **Backend (Java)**: Arquitetura Hexagonal (Ports & Adapters), seguir
  padrao do pacote `domain/` e `adapters/`
- **ML Service (Python)**: FastAPI com lifespan, cadeia de fallback
  (ML -> LLM -> Heuristica), modelos Pydantic em `app/models/`
- **Frontend (TypeScript/React)**: Componentes em `src/components/`,
  paginas em `src/pages/`, servicos em `src/services/`
- **Estilo**: Seguir as ferramentas de lint do projeto:
  - Backend: `mvn spotless:check` (Google Java Format)
  - ML: `ruff check app/`
  - Frontend: `npx tsc --noEmit`

### Documentacao

- Toda documentacao fica em `docs/` no formato Markdown
- Diagramas devem usar Mermaid (nao imagens)
- Portugues como lingua principal
- Atualizar `docs/roadmap.md` ao adicionar/remover funcionalidades

### Commits

Seguir [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: nova funcionalidade
fix: correcao de bug
docs: documentacao
refactor: refatoracao sem mudanca de comportamento
test: adicao ou correcao de testes
ci: alteracao no CI/CD
chore: tarefas administrativas
```

### Branch Strategy

- `main`: Versao estavel, pronto para deploy
- `dev`: Integracao continua, base para novos PRs
- `feat/*`, `fix/*`, `docs/*`: Branches de trabalho

## Ambiente de Desenvolvimento

### Requisitos Minimos

- Docker e Docker Compose
- Git
- Node.js 20 (opcional, para frontend fora do Docker)
- Java 21 + Maven (opcional, para backend fora do Docker)
- Python 3.11 (opcional, para ML service fora do Docker)

### Setup Local

```bash
cp .env.example .env
# Preencha GROQ_API_KEY no .env se quiser testar LLM
docker compose up --build -d
```

### Testes

Os testes serao executados automaticamente no CI. Para executar localmente:

```bash
# Backend
cd backend && mvn test

# ML Service
cd ml-service && pip install -e . && pytest

# Frontend
cd frontend && npm run test
```

## Duvidas?

Abra uma [discussion](https://github.com/DessimA/gambia/discussions)
ou entre em contato pelo [LinkedIn](https://linkedin.com/in/dessima).
