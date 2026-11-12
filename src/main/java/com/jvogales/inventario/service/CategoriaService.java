package com.jvogales.inventario.service;

import com.jvogales.inventario.model.entity.Categoria;
import com.jvogales.inventario.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;

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

        return repository.salvar(existente);
    }

    public void excluir(Long id) {
        buscarPorId(id);
        if (!repository.excluirSeSemVinculos(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O registro possui vínculos com o inventário e não pode ser excluído.");
        }
    }
}
