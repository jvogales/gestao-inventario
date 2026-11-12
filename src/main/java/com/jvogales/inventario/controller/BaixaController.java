package com.jvogales.inventario.controller;
import com.jvogales.inventario.dto.BaixaRequest;
import com.jvogales.inventario.model.entity.Baixa;
import com.jvogales.inventario.service.BaixaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/baixas") @RequiredArgsConstructor
public class BaixaController {
    private final BaixaService service;
    @GetMapping public List<Baixa> listar(){return service.listar();}
    @PostMapping public ResponseEntity<Baixa> registrar(@Valid @RequestBody BaixaRequest dados){return ResponseEntity.status(201).body(service.registrar(dados));}
}
