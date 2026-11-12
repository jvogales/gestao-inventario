package com.jvogales.inventario.controller;

import com.jvogales.inventario.dto.BaixaRequest;
import com.jvogales.inventario.dto.ManutencaoRequest;
import com.jvogales.inventario.dto.MovimentacaoRequest;
import com.jvogales.inventario.model.entity.Baixa;
import com.jvogales.inventario.model.entity.Manutencao;
import com.jvogales.inventario.model.entity.Movimentacao;
import com.jvogales.inventario.service.HistoricoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/equipamentos/{equipamentoId}")
@RequiredArgsConstructor
public class HistoricoController {

    private final HistoricoService service;

    @GetMapping("/movimentacoes")
    public List<Movimentacao> movimentacoes(@PathVariable Long equipamentoId) {
        return service.movimentacoes(equipamentoId);
    }

    @PostMapping("/movimentacoes")
    @ResponseStatus(HttpStatus.CREATED)
    public Movimentacao movimentar(@PathVariable Long equipamentoId, @Valid @RequestBody MovimentacaoRequest dados) {
        return service.movimentar(equipamentoId, dados);
    }

    @GetMapping("/manutencoes")
    public List<Manutencao> manutencoes(@PathVariable Long equipamentoId) {
        return service.manutencoes(equipamentoId);
    }

    @PostMapping("/manutencoes")
    @ResponseStatus(HttpStatus.CREATED)
    public Manutencao registrarManutencao(@PathVariable Long equipamentoId, @Valid @RequestBody ManutencaoRequest dados) {
        return service.registrarManutencao(equipamentoId, dados);
    }

    @PutMapping("/manutencoes/{id}")
    public Manutencao atualizarManutencao(@PathVariable Long equipamentoId, @PathVariable Long id,
                                          @Valid @RequestBody ManutencaoRequest dados) {
        return service.atualizarManutencao(equipamentoId, id, dados);
    }

    @PostMapping("/baixa")
    @ResponseStatus(HttpStatus.CREATED)
    public Baixa baixar(@PathVariable Long equipamentoId, @Valid @RequestBody BaixaRequest dados) {
        return service.baixar(equipamentoId, dados);
    }

    @GetMapping("/baixa")
    public Baixa baixa(@PathVariable Long equipamentoId) {
        return service.buscarBaixa(equipamentoId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Este equipamento ainda não foi baixado."));
    }
}
