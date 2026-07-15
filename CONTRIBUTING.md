# Guia de Contribuição

Obrigado por considerar contribuir com o GambIA! Este documento orienta como
participar do desenvolvimento do projeto.

## Código de Conduta

Ao participar deste projeto, você se compromete a manter um ambiente
respeitoso e colaborativo. Não toleramos assédio ou discriminação de
qualquer natureza.

## Como Contribuir

### Reportar Problemas

1. Verifique se o problema já não foi reportado nas
   [issues](https://github.com/DessimA/gambia/issues)
2. Abra uma nova issue com:
   - Título descritivo
   - Passos para reproduzir
   - Comportamento esperado vs observado
   - Logs ou screenshots, se aplicável
   - Ambiente (SO, versão do Docker, navegador)

### Sugerir Melhorias

Abra uma issue com o rótulo `enhancement` descrevendo:

- O problema que a melhoria resolve
- Como você imagina a solução
- Exemplos de implementação similar (se houver)

### Submeter Alterações (Pull Requests)

1. Faça um fork do repositório
2. Crie uma branch a partir de `dev`:
   ```bash
   git checkout dev
   git checkout -b feat/minha-melhoria
   ```
3. Siga as convenções do projeto (veja abaixo)
4. Commit com mensagens claras em inglês ou português:
   ```bash
   git commit -m "feat: adiciona cálculo de economia anual"
   ```
5. Envie para seu fork e abra um Pull Request para a branch `dev`
6. Aguarde a revisão e a execução do CI (lint)

## Convenções do Projeto

### Código

- **Backend (Java)**: Arquitetura Hexagonal (Ports & Adapters), seguir
  padrão do pacote `domain/` e `adapters/`
- **ML Service (Python)**: FastAPI com lifespan, cadeia de fallback
  (ML -> LLM -> Heurística), modelos Pydantic em `app/models/`
- **Frontend (TypeScript/React)**: Componentes em `src/components/`,
  páginas em `src/pages/`, serviços em `src/services/`
- **Estilo**: Seguir as ferramentas de lint do projeto:
  - Backend: `mvn spotless:check` (Google Java Format)
  - ML: `ruff check app/`
  - Frontend: `npx tsc --noEmit`

### Documentação

- Toda documentação fica em `docs/` no formato Markdown
- Diagramas devem usar Mermaid (não imagens)
- Português como língua principal
- Atualizar `docs/roadmap.md` ao adicionar/remover funcionalidades

### Commits

Seguir [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: nova funcionalidade
fix: correção de bug
docs: documentação
refactor: refatoração sem mudança de comportamento
test: adição ou correção de testes
ci: alteração no CI/CD
chore: tarefas administrativas
```

### Branch Strategy

- `main`: Versão estável, pronto para deploy
- `dev`: Integração contínua, base para novos PRs
- `feat/*`, `fix/*`, `docs/*`: Branches de trabalho

## Ambiente de Desenvolvimento

### Requisitos Mínimos

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

Os testes serão executados automaticamente no CI. Para executar localmente:

```bash
# Backend
cd backend && mvn test

# ML Service
cd ml-service && pip install -e . && pytest

# Frontend
cd frontend && npm run test
```

## Dúvidas?

Abra uma [discussion](https://github.com/DessimA/gambia/discussions)
ou entre em contato pelo [LinkedIn](https://linkedin.com/in/dessima).
