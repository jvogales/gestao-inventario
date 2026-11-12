package com.jvogales.inventario.service;

import com.jvogales.inventario.dto.EquipamentoRequest;
import com.jvogales.inventario.model.entity.Equipamento;
import com.jvogales.inventario.model.enums.EstadoEquipamento;
import com.jvogales.inventario.repository.EquipamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository repository;
    private final CategoriaService categoriaService;
    private final LocalizacaoService localizacaoService;

    public List<Equipamento> listar(String pesquisa, Long categoriaId, Long localizacaoId,
                                    EstadoEquipamento estado) {
        return repository.listar(pesquisa == null ? "" : pesquisa.strip(), categoriaId, localizacaoId, estado);
    }

    public Equipamento buscarPorId(Long id) {
        return repository.buscarPorId(id).orElseThrow(EquipamentoService::naoEncontrado);
    }

    @Transactional
    public Equipamento cadastrar(EquipamentoRequest dados) {
        validarEstado(dados.getEstado());
        return salvar(new Equipamento(null, dados.getNumeroPatrimonio().strip(), dados.getIdentificacao(),
                dados.getMarca(), dados.getModelo(), dados.getObservacao(), dados.getEstado(),
                categoriaService.buscarPorId(dados.getCategoriaId()),
                localizacaoService.buscarPorId(dados.getLocalizacaoId())));
    }

    @Transactional
    public Equipamento atualizar(Long id, EquipamentoRequest dados) {
        var atual = bloquearAtivo(id);
        validarEstado(dados.getEstado());
        if (!atual.getLocalizacaoAtual().getId().equals(dados.getLocalizacaoId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Use a movimentação para alterar a localização.");
        }
        return salvar(new Equipamento(id, dados.getNumeroPatrimonio().strip(), dados.getIdentificacao(),
                dados.getMarca(), dados.getModelo(), dados.getObservacao(), dados.getEstado(),
                categoriaService.buscarPorId(dados.getCategoriaId()), atual.getLocalizacaoAtual()));
    }

    @Transactional
    public Equipamento alterarEstado(Long id, EstadoEquipamento estado) {
        var atual = bloquearAtivo(id);
        validarEstado(estado);
        repository.atualizarEstado(id, estado);
        atual.setEstado(estado);
        return atual;
    }

    Equipamento bloquearAtivo(Long id) {
        var atual = repository.bloquearPorId(id).orElseThrow(EquipamentoService::naoEncontrado);
        if (atual.getEstado() == EstadoEquipamento.BAIXADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este equipamento já foi baixado.");
        }
        return atual;
    }

    private Equipamento salvar(Equipamento equipamento) {
        try {
            return repository.salvar(equipamento);
        } catch (DuplicateKeyException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Número de patrimônio já cadastrado.", ex);
        }
    }

    private void validarEstado(EstadoEquipamento estado) {
        if (estado == EstadoEquipamento.BAIXADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Use o registro de baixa para baixar um equipamento.");
        }
    }

    private static ResponseStatusException naoEncontrado() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado.");
    }
}
