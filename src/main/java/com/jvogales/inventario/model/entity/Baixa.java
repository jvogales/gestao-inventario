package com.jvogales.inventario.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Baixa {
    private Long id;
    private Equipamento equipamento;
    private LocalDate data;
    private String motivo;
    private String observacao;
}
