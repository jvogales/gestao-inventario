package com.jvogales.inventario.model.entity;

import com.jvogales.inventario.model.enums.EstadoEquipamento;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValidacaoEntidadesTests {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void fecharValidador() {
        factory.close();
    }

    @Test
    void permiteEquipamentoCompletoAntesDeGerarId() {
        var equipamento = equipamentoValido();

        assertThat(validator.validate(equipamento)).isEmpty();
        assertThat(equipamento.getCategoria().getNome()).isEqualTo("Informática");
        assertThat(equipamento.getLocalizacaoAtual().getLocal()).isEqualTo("Laboratório 1");
    }

    @Test
    void exigeIdentificacaoPatrimonioEstadoCategoriaELocalizacao() {
        var equipamento = new Equipamento();
        equipamento.setNumeroPatrimonio("   ");
        equipamento.setIdentificacao("\t");

        assertThat(validator.validate(equipamento))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactlyInAnyOrder("numeroPatrimonio", "identificacao", "estado",
                        "categoria", "localizacaoAtual");
    }

    @Test
    void validaDadosDasReferenciasObrigatorias() {
        var equipamento = equipamentoValido();
        equipamento.getCategoria().setNome(" ");
        equipamento.getLocalizacaoAtual().setBloco(null);
        equipamento.getLocalizacaoAtual().setLocal("");

        assertThat(validator.validate(equipamento))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactlyInAnyOrder("categoria.nome", "localizacaoAtual.bloco",
                        "localizacaoAtual.local");
    }

    private Equipamento equipamentoValido() {
        var categoria = new Categoria(null, "Informática", null);
        var localizacao = new Localizacao(null, "A", "Laboratório 1", null);
        return new Equipamento(null, "PAT-001", "Computador do laboratório",
                null, null, null, EstadoEquipamento.DISPONIVEL, categoria, localizacao);
    }
}
