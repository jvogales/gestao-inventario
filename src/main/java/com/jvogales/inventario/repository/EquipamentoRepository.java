package com.jvogales.inventario.repository;

import com.jvogales.inventario.model.entity.Equipamento;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** Armazenamento temporário: os dados são perdidos ao reiniciar a aplicação. */
@Repository
public class EquipamentoRepository {

    private final ConcurrentHashMap<Long, Equipamento> dados = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    public List<Equipamento> listar() {
        return dados.values().stream()
                .sorted(Comparator.comparing(Equipamento::getId))
                .toList();
    }

    public Optional<Equipamento> buscarPorId(Long id) {
        return Optional.ofNullable(dados.get(id));
    }

    public Equipamento salvar(Equipamento entidade) {
        if (entidade.getId() == null) {
            entidade.setId(sequencia.incrementAndGet());
        }
        dados.put(entidade.getId(), entidade);
        return entidade;
    }

    public void excluir(Long id) {
        dados.remove(id);
    }
}
