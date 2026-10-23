package com.jvogales.inventario.dto;

import com.jvogales.inventario.model.enums.EstadoEquipamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/** Dados de entrada; categoria e localização são referenciadas pelos seus IDs. */
@Data
public class EquipamentoRequest {

    @NotBlank(message = "O número de patrimônio é obrigatório.")
    private String numeroPatrimonio;

    @NotBlank(message = "A identificação é obrigatória.")
    private String identificacao;

    private String marca;
    private String modelo;
    private String observacao;

    @NotNull(message = "O estado é obrigatório.")
    private EstadoEquipamento estado;

    @NotNull(message = "O ID da categoria é obrigatório.")
    @Positive(message = "O ID da categoria deve ser positivo.")
    private Long categoriaId;

    @NotNull(message = "O ID da localização é obrigatório.")
    @Positive(message = "O ID da localização deve ser positivo.")
    private Long localizacaoId;
}
