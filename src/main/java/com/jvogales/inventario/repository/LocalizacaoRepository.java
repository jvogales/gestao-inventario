package com.jvogales.inventario.repository;

import com.jvogales.inventario.model.entity.Localizacao;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocalizacaoRepository {

    private static final RowMapper<Localizacao> MAPEAR = (rs, linha) -> new Localizacao(
            rs.getLong("id"), rs.getString("bloco"), rs.getString("local"), rs.getString("descricao"));

    private final JdbcTemplate jdbc;

    public List<Localizacao> listar() {
        return jdbc.query("SELECT * FROM localizacao ORDER BY id", MAPEAR);
    }

    public Optional<Localizacao> buscarPorId(Long id) {
        return jdbc.query("SELECT * FROM localizacao WHERE id = ?", MAPEAR, id).stream().findFirst();
    }

    public Localizacao salvar(Localizacao localizacao) {
        if (localizacao.getId() == null) {
            localizacao.setId(jdbc.queryForObject("""
                    INSERT INTO localizacao (bloco, local, descricao) VALUES (?, ?, ?) RETURNING id
                    """, Long.class, localizacao.getBloco(), localizacao.getLocal(), localizacao.getDescricao()));
        } else {
            jdbc.update("UPDATE localizacao SET bloco = ?, local = ?, descricao = ? WHERE id = ?",
                    localizacao.getBloco(), localizacao.getLocal(), localizacao.getDescricao(), localizacao.getId());
        }
        return localizacao;
    }

    public boolean excluirSeSemVinculos(Long id) {
        try {
            return jdbc.update("""
                    DELETE FROM localizacao l WHERE l.id = ?
                    AND NOT EXISTS (SELECT 1 FROM equipamento e WHERE e.localizacao_id = l.id)
                    AND NOT EXISTS (SELECT 1 FROM movimentacao m WHERE m.origem_id = l.id OR m.destino_id = l.id)
                    """, id) > 0;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }
    }
}
