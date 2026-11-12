package com.jvogales.inventario.model.entity;

import com.jvogales.inventario.model.enums.StatusManutencao;
import com.jvogales.inventario.model.enums.TipoManutencao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manutencao {
    private Long id;
    private Equipamento equipamento;
    private TipoManutencao tipo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String descricao;
    private StatusManutencao status;
    private String observacao;
}
