package com.jvogales.inventario.dto;
import com.jvogales.inventario.model.enums.TipoManutencao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
@Data public class ManutencaoRequest {
    @NotNull private Long equipamentoId;
    @NotNull private TipoManutencao tipo;
    private LocalDate dataInicio;
    @NotBlank private String descricao;
    private String observacao;
}
