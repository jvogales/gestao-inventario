package com.jvogales.inventario.dto;

import com.jvogales.inventario.model.enums.StatusManutencao;
import com.jvogales.inventario.model.enums.TipoManutencao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ManutencaoRequest(@NotNull(message = "Informe o tipo.") TipoManutencao tipo,
                                @NotNull(message = "Informe a data de início.")
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
                                @NotBlank(message = "Informe a descrição.") String descricao,
                                @NotNull(message = "Informe o status.") StatusManutencao status,
                                String observacao) { }
