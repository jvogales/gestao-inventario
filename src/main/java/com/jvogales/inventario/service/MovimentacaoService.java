package com.jvogales.inventario.service;
import com.jvogales.inventario.dto.MovimentacaoRequest;
import com.jvogales.inventario.model.entity.Movimentacao;
import com.jvogales.inventario.model.enums.EstadoEquipamento;
import com.jvogales.inventario.repository.EquipamentoRepository;
import com.jvogales.inventario.repository.MovimentacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
@Service @RequiredArgsConstructor
public class MovimentacaoService {
    private final MovimentacaoRepository repository; private final EquipamentoService equipamentos;
    private final LocalizacaoService localizacoes; private final EquipamentoRepository equipamentoRepository;
    public List<Movimentacao> listar(Long equipamentoId){return repository.listar(equipamentoId);}
    @Transactional public Movimentacao registrar(MovimentacaoRequest dados){var e=equipamentos.buscarPorId(dados.getEquipamentoId());if(e.getEstado()== EstadoEquipamento.BAIXADO)throw new ResponseStatusException(HttpStatus.CONFLICT,"Equipamento baixado não pode ser movimentado.");var destino=localizacoes.buscarPorId(dados.getLocalizacaoDestinoId());if(e.getLocalizacaoAtual().getId().equals(destino.getId()))throw new ResponseStatusException(HttpStatus.CONFLICT,"O destino deve ser diferente da localização atual.");var m=new Movimentacao(null,e,e.getLocalizacaoAtual(),destino, LocalDateTime.now(),dados.getObservacao());repository.salvar(m);equipamentoRepository.atualizarLocalizacao(e.getId(),destino.getId());return m;}
}
