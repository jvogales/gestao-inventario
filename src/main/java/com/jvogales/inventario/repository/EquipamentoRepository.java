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

    private static final String CONSULTA = """
            SELECT e.*, c.nome AS categoria_nome, c.descricao AS categoria_descricao,
                   l.bloco, l.local, l.descricao AS localizacao_descricao
            FROM equipamento e
            JOIN categoria c ON c.id = e.categoria_id
            JOIN localizacao l ON l.id = e.localizacao_id
            """;

    private static final RowMapper<Equipamento> MAPEAR = (rs, linha) -> new Equipamento(
            rs.getLong("id"), rs.getString("numero_patrimonio"), rs.getString("identificacao"),
            rs.getString("marca"), rs.getString("modelo"), rs.getString("observacao"),
            EstadoEquipamento.valueOf(rs.getString("estado")),
            new Categoria(rs.getLong("categoria_id"), rs.getString("categoria_nome"),
                    rs.getString("categoria_descricao")),
            new Localizacao(rs.getLong("localizacao_id"), rs.getString("bloco"),
                    rs.getString("local"), rs.getString("localizacao_descricao")));

    private final JdbcTemplate jdbc;

    public List<Equipamento> listar(String termo, Long categoriaId, Long localizacaoId, EstadoEquipamento estado) {
        return jdbc.query(CONSULTA + """
                WHERE (strpos(lower(e.numero_patrimonio), lower(?)) > 0
                    OR strpos(lower(e.identificacao), lower(?)) > 0)
                  AND e.categoria_id = COALESCE(?, e.categoria_id)
                  AND e.localizacao_id = COALESCE(?, e.localizacao_id)
                  AND e.estado = COALESCE(?, e.estado)
                ORDER BY e.id
                """, MAPEAR, termo, termo, categoriaId, localizacaoId, estado == null ? null : estado.name());
    }

    public Optional<Equipamento> buscarPorId(Long id) {
        return jdbc.query(CONSULTA + "WHERE e.id = ?", MAPEAR, id).stream().findFirst();
    }

    public Optional<Equipamento> bloquearPorId(Long id) {
        // Após o bloqueio, lê a versão confirmada pela operação anterior.
        var bloqueado = jdbc.queryForList("SELECT id FROM equipamento WHERE id = ? FOR UPDATE", Long.class, id);
        return bloqueado.isEmpty() ? Optional.empty() : buscarPorId(id);
    }

    public Equipamento salvar(Equipamento e) {
        if (e.getId() == null) {
            e.setId(jdbc.queryForObject("""
                    INSERT INTO equipamento (numero_patrimonio, identificacao, marca, modelo, observacao,
                                             estado, categoria_id, localizacao_id)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id
                    """, Long.class, e.getNumeroPatrimonio(), e.getIdentificacao(), e.getMarca(), e.getModelo(),
                    e.getObservacao(), e.getEstado().name(), e.getCategoria().getId(), e.getLocalizacaoAtual().getId()));
        } else {
            jdbc.update("""
                    UPDATE equipamento SET numero_patrimonio = ?, identificacao = ?, marca = ?, modelo = ?,
                                           observacao = ?, estado = ?, categoria_id = ? WHERE id = ?
                    """, e.getNumeroPatrimonio(), e.getIdentificacao(), e.getMarca(), e.getModelo(),
                    e.getObservacao(), e.getEstado().name(), e.getCategoria().getId(), e.getId());
        }
        return e;
    }

    public void atualizarLocalizacao(Long id, Long destinoId) {
        jdbc.update("UPDATE equipamento SET localizacao_id = ? WHERE id = ?", destinoId, id);
    }

    public void atualizarEstado(Long id, EstadoEquipamento estado) {
        jdbc.update("UPDATE equipamento SET estado = ? WHERE id = ?", estado.name(), id);
    }
}
