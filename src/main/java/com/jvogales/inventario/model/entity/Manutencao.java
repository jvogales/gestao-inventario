package com.jvogales.inventario.model.entity;

import com.jvogales.inventario.model.enums.StatusManutencao;
import com.jvogales.inventario.model.enums.TipoManutencao;

import java.time.LocalDate;

public record Manutencao(Long id, Long equipamentoId, TipoManutencao tipo, LocalDate dataInicio,
                         LocalDate dataFim, String descricao, StatusManutencao status, String observacao) { }
