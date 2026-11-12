package com.jvogales.inventario.controller;
import com.jvogales.inventario.dto.ManutencaoRequest;
import com.jvogales.inventario.model.entity.Manutencao;
import com.jvogales.inventario.service.ManutencaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/manutencoes") @RequiredArgsConstructor
public class ManutencaoController {
    private final ManutencaoService service;
    @GetMapping public List<Manutencao> listar(@RequestParam(required=false) Long equipamentoId){return service.listar(equipamentoId);}
    @PostMapping public ResponseEntity<Manutencao> registrar(@Valid @RequestBody ManutencaoRequest dados){return ResponseEntity.status(201).body(service.registrar(dados));}
    @PutMapping("/{id}/concluir") public Manutencao concluir(@PathVariable Long id){return service.concluir(id);}
}
