package com.jvogales.inventario.repository;

import com.jvogales.inventario.dto.BaixaRequest;
import com.jvogales.inventario.dto.ManutencaoRequest;
import com.jvogales.inventario.model.entity.Baixa;
import com.jvogales.inventario.model.entity.Localizacao;
import com.jvogales.inventario.model.entity.Manutencao;
import com.jvogales.inventario.model.entity.Movimentacao;
import com.jvogales.inventario.model.enums.StatusManutencao;
import com.jvogales.inventario.model.enums.TipoManutencao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HistoricoRepository {

    private static final String CONSULTA_MOVIMENTACOES = """
            SELECT m.*, o.bloco AS origem_bloco, o.local AS origem_local, o.descricao AS origem_descricao,
                   d.bloco AS destino_bloco, d.local AS destino_local, d.descricao AS destino_descricao
            FROM movimentacao m
            JOIN localizacao o ON o.id = m.origem_id
            JOIN localizacao d ON d.id = m.destino_id
            """;
    private static final RowMapper<Movimentacao> MOVIMENTACAO = (rs, linha) -> new Movimentacao(
            rs.getLong("id"), rs.getLong("equipamento_id"),
            new Localizacao(rs.getLong("origem_id"), rs.getString("origem_bloco"),
                    rs.getString("origem_local"), rs.getString("origem_descricao")),
            new Localizacao(rs.getLong("destino_id"), rs.getString("destino_bloco"),
                    rs.getString("destino_local"), rs.getString("destino_descricao")),
            rs.getObject("data_hora", LocalDateTime.class), rs.getString("observacao"));

    private static final RowMapper<Manutencao> MANUTENCAO = (rs, linha) -> new Manutencao(
            rs.getLong("id"), rs.getLong("equipamento_id"), TipoManutencao.valueOf(rs.getString("tipo")),
            rs.getObject("data_inicio", LocalDate.class), rs.getObject("data_fim", LocalDate.class),
            rs.getString("descricao"), StatusManutencao.valueOf(rs.getString("status")), rs.getString("observacao"));

    private static final RowMapper<Baixa> BAIXA = (rs, linha) -> new Baixa(rs.getLong("id"),
            rs.getLong("equipamento_id"), rs.getObject("data", LocalDate.class),
            rs.getString("motivo"), rs.getString("observacao"));

    private final JdbcTemplate jdbc;

    public Movimentacao registrarMovimentacao(Long equipamentoId, Localizacao origem,
                                             Localizacao destino, String observacao) {
        return jdbc.queryForObject("""
                INSERT INTO movimentacao (equipamento_id, origem_id, destino_id, observacao)
                VALUES (?, ?, ?, ?) RETURNING id, data_hora
                """, (rs, linha) -> new Movimentacao(rs.getLong("id"), equipamentoId, origem, destino,
                rs.getObject("data_hora", LocalDateTime.class), observacao),
                equipamentoId, origem.getId(), destino.getId(), observacao);
    }

    public List<Movimentacao> listarMovimentacoes(Long equipamentoId) {
        return jdbc.query(CONSULTA_MOVIMENTACOES + "WHERE m.equipamento_id = ? ORDER BY m.id DESC",
                MOVIMENTACAO, equipamentoId);
    }

    public Manutencao registrarManutencao(Long equipamentoId, ManutencaoRequest dados) {
        return jdbc.queryForObject("""
                INSERT INTO manutencao (equipamento_id, tipo, data_inicio, data_fim, descricao, status, observacao)
                VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING *
                """, MANUTENCAO, equipamentoId, dados.tipo().name(), dados.dataInicio(), dados.dataFim(),
                dados.descricao(), dados.status().name(), dados.observacao());
    }

    public Optional<Manutencao> atualizarManutencao(Long equipamentoId, Long id, ManutencaoRequest dados) {
        return jdbc.query("""
                UPDATE manutencao SET tipo = ?, data_inicio = ?, data_fim = ?, descricao = ?, status = ?, observacao = ?
                WHERE equipamento_id = ? AND id = ? RETURNING *
                """, MANUTENCAO, dados.tipo().name(), dados.dataInicio(), dados.dataFim(),
                dados.descricao(), dados.status().name(), dados.observacao(), equipamentoId, id).stream().findFirst();
    }

    public List<Manutencao> listarManutencoes(Long equipamentoId) {
        return jdbc.query("SELECT * FROM manutencao WHERE equipamento_id = ? ORDER BY id DESC", MANUTENCAO,
                equipamentoId);
    }

    public Optional<Manutencao> buscarManutencao(Long equipamentoId, Long id) {
        return jdbc.query("SELECT * FROM manutencao WHERE equipamento_id = ? AND id = ?", MANUTENCAO,
                equipamentoId, id).stream().findFirst();
    }

    public Baixa registrarBaixa(Long equipamentoId, BaixaRequest dados) {
        return jdbc.queryForObject("""
                INSERT INTO baixa (equipamento_id, data, motivo, observacao) VALUES (?, ?, ?, ?) RETURNING *
                """, BAIXA, equipamentoId, dados.data(), dados.motivo(), dados.observacao());
    }

    public Optional<Baixa> buscarBaixa(Long equipamentoId) {
        return jdbc.query("SELECT * FROM baixa WHERE equipamento_id = ?", BAIXA, equipamentoId)
                .stream().findFirst();
    }
}
