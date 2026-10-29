CREATE UNIQUE INDEX IF NOT EXISTS uq_categoria_nome_normalizado
ON categoria (LOWER(TRIM(nome)));