package com.jvogales.inventario.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data public class MovimentacaoRequest {
    @NotNull private Long equipamentoId;
    @NotNull private Long localizacaoDestinoId;
    private String observacao;
}
