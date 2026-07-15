# GambIA - Documentação do Projeto

> Inteligência Artificial para Otimização e Eficiência Energética
> Protótipo conceitual desenvolvido como portfólio de engenharia de software e dados.

## Índice de Documentos

| Documento | Descrição |
|-----------|-----------|
| [Arquitetura](architecture.md) | Visão geral da arquitetura de microsserviços, fluxo de dados e componentes |
| [Backend](backend.md) | API Spring Boot (Java 21), Arquitetura Hexagonal (Ports & Adapters) |
| [ML Service](ml-service.md) | Serviço de Machine Learning (FastAPI/Python 3.11), cadeia de fallback |
| [Frontend](frontend.md) | Interface React (TypeScript, Vite), tema Energia Verde |
| [Infraestrutura](infrastructure.md) | Docker Compose, OpenTelemetry, Prometheus, Grafana |
| [Segurança](security.md) | JWT, CSRF Double Submit Cookie, CORS |
| [Banco de Dados](database.md) | Schema PostgreSQL, Flyway migrations, modelo ER |
| [Roadmap](roadmap.md) | Funcionalidades implementadas vs planejadas |
| [Contribuição](../CONTRIBUTING.md) | Guia de contribuição e padrões de desenvolvimento |
| [Segurança](../SECURITY.md) | Política de segurança e reporte de vulnerabilidades |
| [Licença](../LICENSE) | Licença MIT |

## Documentos Raiz

| Arquivo | Descrição |
|---------|-----------|
| [README.md](../README.md) | Visão geral, especificação e manual de execução |
| [CONTRIBUTING.md](../CONTRIBUTING.md) | Guia de contribuição |
| [SECURITY.md](../SECURITY.md) | Política de segurança |
| [LICENSE](../LICENSE) | Licença MIT |

## Proposito

GambIA é um sistema de análise de eficiência energética que cruza dados de
consumo residencial/comercial com um classificador inteligente (Random Forest)
e um modelo de linguagem (Groq LLM), fornecendo diagnóstico, recomendações
personalizadas e estimativa financeira.

## Repositorio

```text
https://github.com/DessimA/gambia
```

## Stack Tecnológica

| Camada | Tecnologia |
|--------|------------|
| Backend | Java 21, Spring Boot 3.3.x, Maven |
| ML Service | Python 3.11, FastAPI, scikit-learn, Groq API |
| Frontend | React 18, TypeScript, Vite, lucide-react |
| Database | PostgreSQL 16, Flyway migrations |
| Infra | Docker Compose, OpenTelemetry, Prometheus, Grafana |
| CI | GitHub Actions (Spotless, Ruff, tsc) |

## Convenções

- Português como língua principal para nomes de campos, mensagens de erro e documentação
- Tabelas com prefixo `tb_`
- UUID como chave primária
- Valores de `tipo_imovel`: Casa, Apartamento, Comércio, Indústria, Rural, Outro
- Tarifa de referência: R$ 0,75/kWh
- Fator de emissão CO2: 0,0385 kg/kWh
