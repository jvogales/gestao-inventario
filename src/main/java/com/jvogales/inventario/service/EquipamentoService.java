package com.jvogales.inventario.service;

import com.jvogales.inventario.dto.EquipamentoRequest;
import com.jvogales.inventario.model.entity.Equipamento;
import com.jvogales.inventario.model.enums.EstadoEquipamento;
import com.jvogales.inventario.repository.EquipamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository repository;
    private final CategoriaService categoriaService;
    private final LocalizacaoService localizacaoService;

    public List<Equipamento> listar(String pesquisa, Long categoriaId, Long localizacaoId,
                                    EstadoEquipamento estado) {
        String termo = pesquisa == null ? "" : pesquisa.strip().toLowerCase(Locale.ROOT);
        return repository.listar().stream()
                .filter(e -> e.getNumeroPatrimonio().toLowerCase(Locale.ROOT).contains(termo)
                        || e.getIdentificacao().toLowerCase(Locale.ROOT).contains(termo))
                .filter(e -> categoriaId == null || e.getCategoria().getId().equals(categoriaId))
                .filter(e -> localizacaoId == null || e.getLocalizacaoAtual().getId().equals(localizacaoId))
                .filter(e -> estado == null || e.getEstado() == estado)
                .toList();
    }

    public Equipamento buscarPorId(Long id) {
        return repository.buscarPorId(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado."));
    }

    public synchronized Equipamento cadastrar(EquipamentoRequest dados) {
        return salvar(null, dados);
    }

    public synchronized Equipamento atualizar(Long id, EquipamentoRequest dados) {
        buscarPorId(id);
        return salvar(id, dados);
    }

    private Equipamento salvar(Long id, EquipamentoRequest dados) {
        String patrimonio = dados.getNumeroPatrimonio().strip();
        if (repository.existePatrimonio(patrimonio, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Número de patrimônio já cadastrado.");
        }

        var categoria = categoriaService.buscarPorId(dados.getCategoriaId());
        var localizacao = localizacaoService.buscarPorId(dados.getLocalizacaoId());
        var equipamento = new Equipamento(id, patrimonio, dados.getIdentificacao(),
                dados.getMarca(), dados.getModelo(), dados.getObservacao(),
                dados.getEstado(), categoria, localizacao);
        return repository.salvar(equipamento);
    }

    /** Exclusão do CRUD didático; não representa a baixa patrimonial (RF22). */
    public synchronized void excluir(Long id) {
        buscarPorId(id);
        repository.excluir(id);
    }
}
