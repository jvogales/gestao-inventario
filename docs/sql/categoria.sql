-- Execute conectado ao banco gestao_inventario.
-- Não remove nem recria uma tabela existente; confira seu schema se ela já existir.
CREATE TABLE IF NOT EXISTS public.categoria (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome TEXT NOT NULL CHECK (btrim(nome) <> ''),
    descricao TEXT
);
