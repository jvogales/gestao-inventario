package com.jvogales.inventario.service;
import com.jvogales.inventario.dto.ManutencaoRequest;
import com.jvogales.inventario.model.entity.Manutencao;
import com.jvogales.inventario.model.enums.EstadoEquipamento;
import com.jvogales.inventario.model.enums.StatusManutencao;
import com.jvogales.inventario.repository.EquipamentoRepository;
import com.jvogales.inventario.repository.ManutencaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;
@Service @RequiredArgsConstructor
public class ManutencaoService {
    private final ManutencaoRepository repository;private final EquipamentoService equipamentos;private final EquipamentoRepository equipamentoRepository;
    public List<Manutencao> listar(Long equipamentoId){return repository.listar(equipamentoId);}
    @Transactional public Manutencao registrar(ManutencaoRequest d){var e=equipamentos.buscarPorId(d.getEquipamentoId());if(e.getEstado()==EstadoEquipamento.BAIXADO)throw new ResponseStatusException(HttpStatus.CONFLICT,"Equipamento baixado não pode receber manutenção.");var m=new Manutencao(null,e,d.getTipo(),d.getDataInicio()==null?LocalDate.now():d.getDataInicio(),null,d.getDescricao(),StatusManutencao.ABERTA,d.getObservacao());repository.salvar(m);equipamentoRepository.atualizarEstado(e.getId(),EstadoEquipamento.EM_MANUTENCAO);return m;}
    @Transactional public Manutencao concluir(Long id){var m=repository.buscarPorId(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Manutenção não encontrada."));if(m.getStatus()!=StatusManutencao.ABERTA)throw new ResponseStatusException(HttpStatus.CONFLICT,"A manutenção já foi encerrada.");m.setStatus(StatusManutencao.CONCLUIDA);m.setDataFim(LocalDate.now());repository.salvar(m);equipamentoRepository.atualizarEstado(m.getEquipamento().getId(),EstadoEquipamento.DISPONIVEL);return m;}
}
