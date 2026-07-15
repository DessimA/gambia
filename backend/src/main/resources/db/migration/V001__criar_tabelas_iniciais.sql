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
    categoria VARCHAR(30) NOT NULL,
    probabilidade NUMERIC(5, 4) NOT NULL,
    custo_estimado_mensal NUMERIC(10, 2) NOT NULL,
    emissao_co2_kg NUMERIC(10, 3) NOT NULL,
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
