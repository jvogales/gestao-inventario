package com.jvogales.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record BaixaRequest(@NotNull(message = "Informe a data da baixa.")
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                           @NotBlank(message = "Informe o motivo.") String motivo, String observacao) { }
