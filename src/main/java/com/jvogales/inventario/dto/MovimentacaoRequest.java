package com.jvogales.inventario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovimentacaoRequest(@NotNull(message = "Informe o destino.")
                                  @Positive(message = "Selecione um destino válido.") Long destinoId,
                                  String observacao) { }
