package com.jvogales.inventario.model.entity;

import java.time.LocalDateTime;

public record Movimentacao(Long id, Long equipamentoId, Localizacao origem, Localizacao destino,
                           LocalDateTime dataHora, String observacao) { }
