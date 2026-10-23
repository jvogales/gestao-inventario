package com.jvogales.inventario.model.entity;

import jakarta.validation.constraints.NotBlank;

/**
 * Local físico onde um equipamento pode estar alocado.
 */
public class Localizacao {

    private Long id;

    @NotBlank(message = "O bloco é obrigatório.")
    private String bloco;

    @NotBlank(message = "O local é obrigatório.")
    private String local;

    private String descricao;

    public Localizacao() {
    }

    public Localizacao(Long id, String bloco, String local, String descricao) {
        this.id = id;
        this.bloco = bloco;
        this.local = local;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
