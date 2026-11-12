package com.jvogales.inventario.model.entity;

import java.time.LocalDate;

public record Baixa(Long id, Long equipamentoId, LocalDate data, String motivo, String observacao) { }
