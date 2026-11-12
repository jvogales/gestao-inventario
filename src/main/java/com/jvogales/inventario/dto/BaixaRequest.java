package com.jvogales.inventario.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
@Data public class BaixaRequest {
    @NotNull private Long equipamentoId;
    private LocalDate data;
    @NotBlank private String motivo;
    private String observacao;
}
