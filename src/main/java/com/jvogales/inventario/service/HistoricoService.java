package com.jvogales.inventario.service;

import com.jvogales.inventario.dto.BaixaRequest;
import com.jvogales.inventario.dto.ManutencaoRequest;
import com.jvogales.inventario.dto.MovimentacaoRequest;
import com.jvogales.inventario.model.entity.Baixa;
import com.jvogales.inventario.model.entity.Manutencao;
import com.jvogales.inventario.model.entity.Movimentacao;
import com.jvogales.inventario.model.enums.EstadoEquipamento;
import com.jvogales.inventario.model.enums.StatusManutencao;
import com.jvogales.inventario.repository.EquipamentoRepository;
import com.jvogales.inventario.repository.HistoricoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoRepository historico;
    private final EquipamentoRepository equipamentos;
    private final EquipamentoService equipamentoService;
    private final LocalizacaoService localizacaoService;

    @Transactional
    public Movimentacao movimentar(Long equipamentoId, MovimentacaoRequest dados) {
        var equipamento = equipamentoService.bloquearAtivo(equipamentoId);
        var origem = equipamento.getLocalizacaoAtual();
        if (origem.getId().equals(dados.destinoId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Selecione um destino diferente da localização atual.");
        }
        var destino = localizacaoService.buscarPorId(dados.destinoId());
        var movimentacao = historico.registrarMovimentacao(equipamentoId, origem, destino, dados.observacao());
        equipamentos.atualizarLocalizacao(equipamentoId, destino.getId());
        return movimentacao;
    }

    public List<Movimentacao> movimentacoes(Long equipamentoId) {
        equipamentoService.buscarPorId(equipamentoId);
        return historico.listarMovimentacoes(equipamentoId);
    }

    @Transactional
    public Manutencao registrarManutencao(Long equipamentoId, ManutencaoRequest dados) {
        equipamentoService.bloquearAtivo(equipamentoId);
        validarDatas(dados);
        return historico.registrarManutencao(equipamentoId, dados);
    }

    @Transactional
    public Manutencao atualizarManutencao(Long equipamentoId, Long id, ManutencaoRequest dados) {
        equipamentoService.bloquearAtivo(equipamentoId);
        validarDatas(dados);
        return historico.atualizarManutencao(equipamentoId, id, dados)
                .orElseThrow(HistoricoService::manutencaoNaoEncontrada);
    }

    public Manutencao buscarManutencao(Long equipamentoId, Long id) {
        equipamentoService.buscarPorId(equipamentoId);
        return historico.buscarManutencao(equipamentoId, id).orElseThrow(HistoricoService::manutencaoNaoEncontrada);
    }

    public List<Manutencao> manutencoes(Long equipamentoId) {
        equipamentoService.buscarPorId(equipamentoId);
        return historico.listarManutencoes(equipamentoId);
    }

    @Transactional
    public Baixa baixar(Long equipamentoId, BaixaRequest dados) {
        equipamentoService.bloquearAtivo(equipamentoId);
        return registrarBaixa(equipamentoId, dados);
    }

    @Transactional
    public void excluirLegado(Long equipamentoId) {
        var equipamento = equipamentos.bloquearPorId(equipamentoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado."));
        if (equipamento.getEstado() != EstadoEquipamento.BAIXADO) {
            registrarBaixa(equipamentoId, new BaixaRequest(LocalDate.now(), "Baixa pela rota DELETE", null));
        }
    }

    public Optional<Baixa> buscarBaixa(Long equipamentoId) {
        equipamentoService.buscarPorId(equipamentoId);
        return historico.buscarBaixa(equipamentoId);
    }

    private Baixa registrarBaixa(Long equipamentoId, BaixaRequest dados) {
        var baixa = historico.registrarBaixa(equipamentoId, dados);
        equipamentos.atualizarEstado(equipamentoId, EstadoEquipamento.BAIXADO);
        return baixa;
    }

    private void validarDatas(ManutencaoRequest dados) {
        if (dados.dataFim() != null && dados.dataFim().isBefore(dados.dataInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data de fim deve ser igual ou posterior ao início.");
        }
        if (dados.status() == StatusManutencao.CONCLUIDA && dados.dataFim() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a data de fim para concluir a manutenção.");
        }
    }

    private static ResponseStatusException manutencaoNaoEncontrada() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Manutenção não encontrada para este equipamento.");
    }
}
