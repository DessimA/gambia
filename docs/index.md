# GambIA - Documentacao do Projeto

> Inteligencia Artificial para Otimizacao e Eficiencia Energetica
> Prototipo conceitual desenvolvido como portfolio de engenharia de software e dados.

## Indice de Documentos

| Documento | Descricao |
|-----------|-----------|
| [Arquitetura](architecture.md) | Visao geral da arquitetura de microsservicos, fluxo de dados e componentes |
| [Backend](backend.md) | API Spring Boot (Java 21), Arquitetura Hexagonal (Ports & Adapters) |
| [ML Service](ml-service.md) | Servico de Machine Learning (FastAPI/Python 3.11), cadeia de fallback |
| [Frontend](frontend.md) | Interface React (TypeScript, Vite), tema Energia Verde |
| [Infraestrutura](infrastructure.md) | Docker Compose, OpenTelemetry, Prometheus, Grafana |
| [Seguranca](security.md) | JWT, CSRF Double Submit Cookie, CORS |
| [Banco de Dados](database.md) | Schema PostgreSQL, Flyway migrations, modelo ER |
| [Roadmap](roadmap.md) | Funcionalidades implementadas vs planejadas |

## Proposito

GambIA e um sistema de analise de eficiencia energetica que cruza dados de
consumo residencial/comercial com um classificador inteligente (Random Forest)
e um modelo de linguagem (Groq LLM), fornecendo diagnostico, recomendacoes
personalizadas e estimativa financeira.

## Repositorio

```text
https://github.com/DessimA/gambia
```

## Stack Tecnologica

| Camada | Tecnologia |
|--------|------------|
| Backend | Java 21, Spring Boot 3.3.x, Maven |
| ML Service | Python 3.11, FastAPI, scikit-learn, Groq API |
| Frontend | React 18, TypeScript, Vite, lucide-react |
| Database | PostgreSQL 16, Flyway migrations |
| Infra | Docker Compose, OpenTelemetry, Prometheus, Grafana |
| CI | GitHub Actions (Spotless, Ruff, tsc) |

## Convencoes

- Portugues como lingua principal para nomes de campos, mensagens de erro e documentacao
- Tabelas com prefixo `tb_`
- UUID como chave primaria
- Valores de `tipo_imovel`: Casa, Apartamento, Comercio, Industria, Rural, Outro
- Tarifa de referencia: R$ 0,75/kWh
- Fator de emissao CO2: 0,0385 kg/kWh
