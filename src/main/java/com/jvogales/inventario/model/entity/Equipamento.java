package com.jvogales.inventario.model.entity;

import com.jvogales.inventario.model.enums.EstadoEquipamento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Equipamento com sua categoria, localização atual e estado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipamento {

    private Long id;

    @NotBlank(message = "O número de patrimônio é obrigatório.")
    private String numeroPatrimonio;

    @NotBlank(message = "A identificação é obrigatória.")
    private String identificacao;

    private String marca;

    private String modelo;

    private String observacao;

    @NotNull(message = "O estado do equipamento é obrigatório.")
    private EstadoEquipamento estado;

    @NotNull(message = "A categoria é obrigatória.")
    @Valid
    private Categoria categoria;

    @NotNull(message = "A localização atual é obrigatória.")
    @Valid
    private Localizacao localizacaoAtual;
}
