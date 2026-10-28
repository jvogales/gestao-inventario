package com.jvogales.inventario.service;

import com.jvogales.inventario.model.entity.Categoria;
import com.jvogales.inventario.repository.CategoriaRepository;
import com.jvogales.inventario.repository.EquipamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;
    private final EquipamentoRepository equipamentoRepository;

    public List<Categoria> listar() {
        return repository.listar();
    }

    public Categoria buscarPorId(Long id) {
        return repository.buscarPorId(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));
    }

    public Categoria cadastrar(Categoria dados) {
        dados.setId(null);
        return repository.salvar(dados);
    }

    public Categoria atualizar(Long id, Categoria dados) {
        var existente = buscarPorId(id);

        existente.setNome(dados.getNome());
        existente.setDescricao(dados.getDescricao());

        var atualizada = repository.salvar(existente);

        // Sincronização temporária enquanto EquipamentoRepository
        // ainda utiliza armazenamento em memória.
        equipamentoRepository.listar().stream()
            .filter(e -> e.getCategoria().getId().equals(id))
            .forEach(e -> e.setCategoria(atualizada));

        return atualizada;
    }

    public void excluir(Long id) {
        buscarPorId(id);
        boolean emUso = equipamentoRepository.listar().stream()
                .anyMatch(equipamento -> equipamento.getCategoria().getId().equals(id));
        if (emUso) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Categoria vinculada a equipamento não pode ser excluída.");
        }
        repository.excluir(id);
    }
}
