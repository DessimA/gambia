# Banco de Dados

## Tecnologias

- PostgreSQL 16 (container `postgres:16-alpine`)
- Flyway 10.20.x (migrações versionadas)
- UUID como chave primária
- Prefixo `tb_` para todas as tabelas

## Modelo Entidade-Relacionamento

```mermaid
erDiagram
    tb_imovel {
        uuid id PK
        varchar tipo_imovel "NOT NULL"
        int quantidade_equipamentos "NOT NULL, DEFAULT 0"
        timestamptz created_at "DEFAULT CURRENT_TIMESTAMP"
    }

    tb_analise_consumo {
        uuid id PK
        uuid imovel_id FK "NOT NULL"
        numeric consumo_kwh "NOT NULL, (10,2)"
        boolean uso_horario_pico "NOT NULL"
        int horas_alto_consumo "NOT NULL"
        varchar categoria "NOT NULL, (30)"
        numeric probabilidade "NOT NULL, (5,4)"
        numeric custo_estimado_mensal "NOT NULL, (10,2)"
        numeric emissao_co2_kg "NOT NULL, (10,3)"
        timestamptz created_at "DEFAULT CURRENT_TIMESTAMP"
    }

    tb_recomendacao_gerada {
        uuid id PK
        uuid analise_id FK "NOT NULL"
        text recomendacao_texto "NOT NULL"
        timestamptz created_at "DEFAULT CURRENT_TIMESTAMP"
    }

    tb_imovel ||--o{ tb_analise_consumo : "1:N"
    tb_analise_consumo ||--o{ tb_recomendacao_gerada : "1:N"
```

## Migração V001 (Flyway)

```sql
CREATE TABLE tb_imovel (
    id UUID PRIMARY KEY,
    tipo_imovel VARCHAR(50) NOT NULL,
    quantidade_equipamentos INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_analise_consumo (
    id UUID PRIMARY KEY,
    imovel_id UUID REFERENCES tb_imovel(id) ON DELETE CASCADE,
    consumo_kwh NUMERIC(10, 2) NOT NULL,
    uso_horario_pico BOOLEAN NOT NULL,
    horas_alto_consumo INT NOT NULL,
    categoria VARCHAR(30) NOT NULL,           -- Eficiente, Moderado, Ineficiente
    probabilidade NUMERIC(5, 4) NOT NULL,
    custo_estimado_mensal NUMERIC(10, 2) NOT NULL,
    emissao_co2_kg NUMERIC(10, 3) NOT NULL,    -- Fator de emissao SIN
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_analise_imovel ON tb_analise_consumo(imovel_id);

CREATE TABLE tb_recomendacao_gerada (
    id UUID PRIMARY KEY,
    analise_id UUID REFERENCES tb_analise_consumo(id) ON DELETE CASCADE,
    recomendacao_texto TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_recom_analise ON tb_recomendacao_gerada(analise_id);
```

## Descrição das Tabelas

### tb_imovel

Cadastro básico do imóvel analisado.

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| id | UUID | Chave primária |
| tipo_imovel | VARCHAR(50) | Casa, Apartamento, Comércio, Indústria, Rural, Outro |
| quantidade_equipamentos | INT | Quantidade de aparelhos elétricos |
| created_at | TIMESTAMPTZ | Data de criação |

### tb_analise_consumo

Histórico de leituras e estimativas de consumo.

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| id | UUID | Chave primária |
| imovel_id | UUID | FK para tb_imovel (ON DELETE CASCADE) |
| consumo_kwh | NUMERIC(10,2) | Consumo mensal em kWh |
| uso_horario_pico | BOOLEAN | Uso no horário de pico (18h-21h) |
| horas_alto_consumo | INT | Média diária de alto consumo |
| categoria | VARCHAR(30) | Eficiente, Moderado, Ineficiente |
| probabilidade | NUMERIC(5,4) | Confiança da classificação |
| custo_estimado_mensal | NUMERIC(10,2) | R$ (consumo * 0,75) |
| emissao_co2_kg | NUMERIC(10,3) | kg CO2 (consumo * 0,0385) |
| created_at | TIMESTAMPTZ | Data de criação |

Índice: `idx_analise_imovel` em `imovel_id`.

### tb_recomendacao_gerada

Recomendações geradas pelo LLM para cada análise.

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| id | UUID | Chave primária |
| analise_id | UUID | FK para tb_analise_consumo (ON DELETE CASCADE) |
| recomendacao_texto | TEXT | Texto da recomendação |
| created_at | TIMESTAMPTZ | Data de criação |

Índice: `idx_recom_analise` em `analise_id`.

## Configuração Flyway

```yaml
# application.yml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
  jpa:
    hibernate:
      ddl-auto: validate  # Nao cria tabelas, apenas valida
```

As migrações estão em `backend/src/main/resources/db/migration/`.
