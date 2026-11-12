package com.jvogales.inventario.controller;
import com.jvogales.inventario.dto.MovimentacaoRequest;
import com.jvogales.inventario.model.entity.Movimentacao;
import com.jvogales.inventario.service.MovimentacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/movimentacoes") @RequiredArgsConstructor
public class MovimentacaoController {
    private final MovimentacaoService service;
    @GetMapping public List<Movimentacao> listar(@RequestParam(required=false) Long equipamentoId){return service.listar(equipamentoId);}
    @PostMapping public ResponseEntity<Movimentacao> registrar(@Valid @RequestBody MovimentacaoRequest dados){return ResponseEntity.status(201).body(service.registrar(dados));}
}
