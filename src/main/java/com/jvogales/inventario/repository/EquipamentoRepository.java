package com.jvogales.inventario.repository;

import com.jvogales.inventario.model.entity.Categoria;
import com.jvogales.inventario.model.entity.Equipamento;
import com.jvogales.inventario.model.entity.Localizacao;
import com.jvogales.inventario.model.enums.EstadoEquipamento;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EquipamentoRepository {
    private static final String SELECT_BASE = """
            SELECT e.id, e.numero_patrimonio, e.identificacao, e.marca, e.modelo, e.observacao, e.estado,
                   c.id categoria_id, c.nome categoria_nome, c.descricao categoria_descricao,
                   l.id localizacao_id, l.bloco, l.local, l.descricao localizacao_descricao
            FROM equipamento e JOIN categoria c ON c.id=e.categoria_id
            JOIN localizacao l ON l.id=e.localizacao_atual_id
            """;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Equipamento> mapper = (rs, row) -> new Equipamento(
            rs.getLong("id"), rs.getString("numero_patrimonio"), rs.getString("identificacao"),
            rs.getString("marca"), rs.getString("modelo"), rs.getString("observacao"),
            EstadoEquipamento.valueOf(rs.getString("estado")),
            new Categoria(rs.getLong("categoria_id"), rs.getString("categoria_nome"), rs.getString("categoria_descricao")),
            new Localizacao(rs.getLong("localizacao_id"), rs.getString("bloco"), rs.getString("local"),
                    rs.getString("localizacao_descricao")));

    public List<Equipamento> listar() { return jdbcTemplate.query(SELECT_BASE + " ORDER BY e.id", mapper); }
    public Optional<Equipamento> buscarPorId(Long id) {
        return jdbcTemplate.query(SELECT_BASE + " WHERE e.id=?", mapper, id).stream().findFirst();
    }
    public Equipamento salvar(Equipamento e) {
        if (e.getId() == null) {
            Long id = JdbcKeys.insert(jdbcTemplate,"""
                    INSERT INTO equipamento (numero_patrimonio,identificacao,marca,modelo,observacao,estado,categoria_id,localizacao_atual_id)
                    VALUES (?,?,?,?,?,?,?,?)
                    """, e.getNumeroPatrimonio(), e.getIdentificacao(), e.getMarca(), e.getModelo(),
                    e.getObservacao(), e.getEstado().name(), e.getCategoria().getId(), e.getLocalizacaoAtual().getId());
            e.setId(id);
        } else {
            jdbcTemplate.update("""
                    UPDATE equipamento SET numero_patrimonio=?,identificacao=?,marca=?,modelo=?,observacao=?,estado=?,categoria_id=?,localizacao_atual_id=? WHERE id=?
                    """, e.getNumeroPatrimonio(), e.getIdentificacao(), e.getMarca(), e.getModelo(), e.getObservacao(),
                    e.getEstado().name(), e.getCategoria().getId(), e.getLocalizacaoAtual().getId(), e.getId());
        }
        return buscarPorId(e.getId()).orElseThrow();
    }
    public void atualizarLocalizacao(Long equipamentoId, Long localizacaoId) {
        jdbcTemplate.update("UPDATE equipamento SET localizacao_atual_id=? WHERE id=?", localizacaoId, equipamentoId);
    }
    public void atualizarEstado(Long equipamentoId, EstadoEquipamento estado) {
        jdbcTemplate.update("UPDATE equipamento SET estado=? WHERE id=?", estado.name(), equipamentoId);
    }
    public boolean existePorCategoria(Long id) { return count("categoria_id", id); }
    public boolean existePorLocalizacao(Long id) { return count("localizacao_atual_id", id); }
    private boolean count(String coluna, Long id) {
        Integer n=jdbcTemplate.queryForObject("SELECT COUNT(*) FROM equipamento WHERE "+coluna+"=?", Integer.class, id);
        return n != null && n > 0;
    }
    public boolean existePatrimonio(String patrimonio, Long ignorarId) {
        Integer n=jdbcTemplate.queryForObject("SELECT COUNT(*) FROM equipamento WHERE LOWER(numero_patrimonio)=LOWER(?) AND (? IS NULL OR id<>?)",
                Integer.class, patrimonio, ignorarId, ignorarId);
        return n != null && n > 0;
    }
    public void excluir(Long id) { jdbcTemplate.update("DELETE FROM equipamento WHERE id=?", id); }
}
