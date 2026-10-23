package com.jvogales.inventario.controller;

import com.jvogales.inventario.model.entity.Localizacao;
import com.jvogales.inventario.service.LocalizacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/localizacoes")
@RequiredArgsConstructor
public class LocalizacaoController {

    private final LocalizacaoService service;

    @GetMapping
    public ResponseEntity<List<Localizacao>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Localizacao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Localizacao> cadastrar(@Valid @RequestBody Localizacao dados) {
        var cadastrado = service.cadastrar(dados);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(cadastrado.getId()).toUri();
        return ResponseEntity.created(uri).body(cadastrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Localizacao> atualizar(@PathVariable Long id,
                                            @Valid @RequestBody Localizacao dados) {
        return ResponseEntity.ok(service.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
