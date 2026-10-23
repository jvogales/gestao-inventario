package com.jvogales.inventario.model.entity;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Local físico onde um equipamento pode estar alocado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Localizacao {

    private Long id;

    @NotBlank(message = "O bloco é obrigatório.")
    private String bloco;

    @NotBlank(message = "O local é obrigatório.")
    private String local;

    private String descricao;
}
