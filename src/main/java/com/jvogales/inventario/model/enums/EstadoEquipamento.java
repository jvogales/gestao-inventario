package com.jvogales.inventario.model.enums;

/**
 * Proposta inicial de estados, ainda não enumerados nos requisitos e diagramas.
 * As transições e permissões serão definidas na camada de serviço.
 */
public enum EstadoEquipamento {
    DISPONIVEL,
    EM_USO,
    EM_MANUTENCAO,
    BAIXADO
}
