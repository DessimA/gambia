# Arquitetura do Sistema

## Visão Geral

O GambIA opera sob um modelo de microsserviços integrados, distribuído em
três camadas funcionais, todos orquestrados via Docker Compose.

```mermaid
flowchart TD
    subgraph Cliente["Camada de Apresentacao"]
        A["Frontend - React/TypeScript"]
    end

    subgraph Portal["Camada de Orquestracao e Negocio"]
        B["Backend - Spring Boot / Java 21"]
    end

    subgraph IA["Camada de Inteligencia e Inferencia"]
        C["ML API - FastAPI / Python 3.11"]
        D["Modelo Classificador + LLM Groq"]
    end

    subgraph Persistencia["Camada de Dados"]
        E[("PostgreSQL 16")]
    end

    subgraph Obs["Observabilidade"]
        F["OpenTelemetry Collector"]
        G["Prometheus"]
        H["Grafana"]
    end

    A -- "HTTP POST /analise-energetica" --> B
    B -- "Chamada REST interna" --> C
    C -- "Carrega em lifespan" --> D
    B -- "Spring Data JPA / Flyway" --> E
    B -- "Metrias" --> F
    C -- "Metrias" --> F
    F --> G
    G --> H
```

## Fluxo de Dados de uma Requisição

```mermaid
sequenceDiagram
    participant User as Usuario
    participant FE as Frontend (React)
    participant BE as Backend (Spring Boot)
    participant ML as ML Service (FastAPI)
    participant DB as PostgreSQL

    User->>FE: Preenche formulario
    FE->>BE: POST /analise-energetica (JSON)
    BE->>BE: Bean Validation
    BE->>DB: Salva imovel (tb_imovel)
    BE->>ML: POST /classificar (JSON)
    ML->>ML: Cadeia de fallback
    ML-->>BE: Resposta (categoria, recomendacoes)
    BE->>BE: Calcula custo (R$ 0,75/kWh) e CO2
    BE->>DB: Salva analise (tb_analise_consumo)
    BE->>DB: Salva recomendacoes (tb_recomendacao_gerada)
    BE-->>FE: Resposta JSON
    FE->>User: Exibe resultado
```

## Cadeia de Fallback do ML

```mermaid
flowchart LR
    A["Requisicao /classificar"] --> B["Classificador Random Forest"]
    B --> C{Confianca >= 0.80?}
    C -- Sim --> D["Retorna classificacao direta"]
    C -- Nao --> E["LLM Groq (llama-3.3-70b)"]
    E --> F{Sucesso?}
    F -- Sim --> G["Retorna classificacao + recomendacoes"]
    F -- Nao --> H["Heuristica deterministica"]
    H --> I["Retorna regras de negocio"]
```

## Arquitetura Hexagonal (Backend)

```mermaid
flowchart TD
    subgraph Domain["Dominio (Core)"]
        direction TB
        D1["Modelos de Dominio"]
        D2["Portas (interfaces)"]
        D3["Servicos de Dominio"]
    end

    subgraph Adapters["Adaptadores"]
        direction TB
        subgraph In["Inbound Adapters"]
            A1["Controladores REST"]
            A2["DTOs de Entrada"]
            A3["Exception Handler"]
            A4["Filtros de Seguranca"]
        end
        subgraph Out["Outbound Adapters"]
            B1["Client HTTP (ML)"]
            B2["Persistencia JPA"]
            B3["Repositorios Spring Data"]
        end
    end

    A1 --> D2
    D2 --> D3
    D3 --> D2
    D2 --> B1
    D2 --> B2
    B2 --> B3
    B1 --> B1
```

## Portas e URLs de Acesso

| Serviço | URL Local | Container |
|---------|-----------|-----------|
| Frontend | `http://localhost:5173` | gambia-frontend |
| Backend API | `http://localhost:8080` | gambia-backend |
| ML Service | `http://localhost:8000` | gambia-ml |
| Swagger ML | `http://localhost:8000/docs` | gambia-ml |
| Grafana | `http://localhost:3000` | gambia-grafana |
| Prometheus | `http://localhost:9090` | gambia-prometheus |
| PostgreSQL | `localhost:5432` | gambia-postgres |
