package com.jvogales.inventario.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao {
    private Long id;
    private Equipamento equipamento;
    private Localizacao localizacaoOrigem;
    private Localizacao localizacaoDestino;
    private LocalDateTime dataHora;
    private String observacao;
}
