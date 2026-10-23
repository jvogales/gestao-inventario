package com.jvogales.inventario.model.entity;

import jakarta.validation.constraints.NotBlank;

/**
 * Categoria dos equipamentos do inventário.
 */
public class Categoria {

    private Long id;

    @NotBlank(message = "O nome da categoria é obrigatório.")
    private String nome;

    private String descricao;

    public Categoria() {
    }

    public Categoria(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
