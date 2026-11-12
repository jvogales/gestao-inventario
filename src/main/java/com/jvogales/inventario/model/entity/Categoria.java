package com.jvogales.inventario.model.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Categoria dos equipamentos do inventário.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    private Long id;

    @NotBlank(message = "O nome da categoria é obrigatório.")
    @Size(max = 100, message = "Use até 100 caracteres para o nome.")
    private String nome;

    private String descricao;
}
