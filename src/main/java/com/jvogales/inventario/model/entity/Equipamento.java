package com.jvogales.inventario.model.entity;

import com.jvogales.inventario.model.enums.EstadoEquipamento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Equipamento com sua categoria, localização atual e estado.
 */
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

    public Equipamento() {
    }

    public Equipamento(Long id, String numeroPatrimonio, String identificacao,
                       String marca, String modelo, String observacao,
                       EstadoEquipamento estado, Categoria categoria,
                       Localizacao localizacaoAtual) {
        this.id = id;
        this.numeroPatrimonio = numeroPatrimonio;
        this.identificacao = identificacao;
        this.marca = marca;
        this.modelo = modelo;
        this.observacao = observacao;
        this.estado = estado;
        this.categoria = categoria;
        this.localizacaoAtual = localizacaoAtual;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPatrimonio() {
        return numeroPatrimonio;
    }

    public void setNumeroPatrimonio(String numeroPatrimonio) {
        this.numeroPatrimonio = numeroPatrimonio;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public EstadoEquipamento getEstado() {
        return estado;
    }

    public void setEstado(EstadoEquipamento estado) {
        this.estado = estado;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Localizacao getLocalizacaoAtual() {
        return localizacaoAtual;
    }

    public void setLocalizacaoAtual(Localizacao localizacaoAtual) {
        this.localizacaoAtual = localizacaoAtual;
    }
}
