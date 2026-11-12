package com.jvogales.inventario.service;
import com.jvogales.inventario.dto.BaixaRequest;
import com.jvogales.inventario.model.entity.Baixa;
import com.jvogales.inventario.model.enums.EstadoEquipamento;
import com.jvogales.inventario.repository.BaixaRepository;
import com.jvogales.inventario.repository.EquipamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;
@Service @RequiredArgsConstructor
public class BaixaService {
    private final BaixaRepository repository;private final EquipamentoService equipamentos;private final EquipamentoRepository equipamentoRepository;
    public List<Baixa> listar(){return repository.listar();}
    @Transactional public Baixa registrar(BaixaRequest d){var e=equipamentos.buscarPorId(d.getEquipamentoId());if(e.getEstado()==EstadoEquipamento.BAIXADO||repository.buscarPorEquipamento(e.getId()).isPresent())throw new ResponseStatusException(HttpStatus.CONFLICT,"Equipamento já está baixado.");var b=new Baixa(null,e,d.getData()==null?LocalDate.now():d.getData(),d.getMotivo(),d.getObservacao());repository.salvar(b);equipamentoRepository.atualizarEstado(e.getId(),EstadoEquipamento.BAIXADO);return b;}
}
