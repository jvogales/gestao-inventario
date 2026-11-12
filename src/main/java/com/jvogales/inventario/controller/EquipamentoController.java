package com.jvogales.inventario.controller;

import com.jvogales.inventario.model.entity.Equipamento;
import com.jvogales.inventario.service.EquipamentoService;
import com.jvogales.inventario.service.HistoricoService;
import com.jvogales.inventario.dto.EquipamentoRequest;
import com.jvogales.inventario.model.enums.EstadoEquipamento;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/equipamentos")
@RequiredArgsConstructor
public class EquipamentoController {

    private final EquipamentoService service;
    private final HistoricoService historico;

    @GetMapping
    public ResponseEntity<List<Equipamento>> listar(
            @RequestParam(required = false) String pesquisa,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long localizacaoId,
            @RequestParam(required = false) EstadoEquipamento estado) {
        return ResponseEntity.ok(service.listar(pesquisa, categoriaId, localizacaoId, estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipamento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Equipamento> cadastrar(@Valid @RequestBody EquipamentoRequest dados) {
        var cadastrado = service.cadastrar(dados);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(cadastrado.getId()).toUri();
        return ResponseEntity.created(uri).body(cadastrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipamento> atualizar(@PathVariable Long id,
                                            @Valid @RequestBody EquipamentoRequest dados) {
        return ResponseEntity.ok(service.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        historico.excluirLegado(id);
        return ResponseEntity.noContent().build();
    }
}
