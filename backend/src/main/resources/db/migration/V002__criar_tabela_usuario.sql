CREATE TABLE tb_usuario (
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE tb_imovel ADD COLUMN usuario_id UUID REFERENCES tb_usuario(id);
CREATE INDEX idx_imovel_usuario ON tb_imovel(usuario_id);
