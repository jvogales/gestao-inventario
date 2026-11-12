package com.jvogales.inventario.repository;

import com.jvogales.inventario.model.entity.Localizacao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocalizacaoRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Localizacao> mapper = (rs, row) -> new Localizacao(
            rs.getLong("id"), rs.getString("bloco"), rs.getString("local"), rs.getString("descricao"));

    public List<Localizacao> listar() {
        return jdbcTemplate.query("SELECT id, bloco, local, descricao FROM localizacao ORDER BY bloco, local", mapper);
    }

    public Optional<Localizacao> buscarPorId(Long id) {
        return jdbcTemplate.query("SELECT id, bloco, local, descricao FROM localizacao WHERE id = ?", mapper, id)
                .stream().findFirst();
    }

    public Localizacao salvar(Localizacao localizacao) {
        if (localizacao.getId() == null) {
            Long id = JdbcKeys.insert(jdbcTemplate,
                    "INSERT INTO localizacao (bloco, local, descricao) VALUES (?, ?, ?)",
                    localizacao.getBloco(), localizacao.getLocal(), localizacao.getDescricao());
            localizacao.setId(id);
        } else {
            jdbcTemplate.update("UPDATE localizacao SET bloco = ?, local = ?, descricao = ? WHERE id = ?",
                    localizacao.getBloco(), localizacao.getLocal(), localizacao.getDescricao(), localizacao.getId());
        }
        return localizacao;
    }

    public void excluir(Long id) { jdbcTemplate.update("DELETE FROM localizacao WHERE id = ?", id); }
}
