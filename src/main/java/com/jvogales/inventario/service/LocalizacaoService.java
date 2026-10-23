package com.jvogales.inventario.service;

import com.jvogales.inventario.model.entity.Localizacao;
import com.jvogales.inventario.repository.LocalizacaoRepository;
import com.jvogales.inventario.repository.EquipamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalizacaoService {

    private final LocalizacaoRepository repository;
    private final EquipamentoRepository equipamentoRepository;

    public List<Localizacao> listar() {
        return repository.listar();
    }

    public Localizacao buscarPorId(Long id) {
        return repository.buscarPorId(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Localização não encontrada."));
    }

    public Localizacao cadastrar(Localizacao dados) {
        dados.setId(null);
        return repository.salvar(dados);
    }

    public Localizacao atualizar(Long id, Localizacao dados) {
        var existente = buscarPorId(id);
        existente.setBloco(dados.getBloco());
        existente.setLocal(dados.getLocal());
        existente.setDescricao(dados.getDescricao());
        return repository.salvar(existente);
    }

    public void excluir(Long id) {
        buscarPorId(id);
        boolean emUso = equipamentoRepository.listar().stream()
                .anyMatch(equipamento -> equipamento.getLocalizacaoAtual().getId().equals(id));
        if (emUso) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Localização vinculada a equipamento não pode ser excluída.");
        }
        repository.excluir(id);
    }
}
