package com.jvogales.inventario.repository;

import com.jvogales.inventario.model.entity.Movimentacao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository @RequiredArgsConstructor
public class MovimentacaoRepository {
    private final JdbcTemplate jdbc;
    private final EquipamentoRepository equipamentos;
    private final LocalizacaoRepository localizacoes;
    public Movimentacao salvar(Movimentacao m) {
        Long id=JdbcKeys.insert(jdbc,"INSERT INTO movimentacao (equipamento_id,localizacao_origem_id,localizacao_destino_id,data_hora,observacao) VALUES (?,?,?,?,?)",
                m.getEquipamento().getId(),m.getLocalizacaoOrigem().getId(),m.getLocalizacaoDestino().getId(),Timestamp.valueOf(m.getDataHora()),m.getObservacao());
        m.setId(id); return m;
    }
    public List<Movimentacao> listar(Long equipamentoId) {
        String sql="SELECT * FROM movimentacao"+(equipamentoId==null?"":" WHERE equipamento_id=?")+" ORDER BY data_hora DESC";
        Object[] args=equipamentoId==null?new Object[0]:new Object[]{equipamentoId};
        return jdbc.query(sql,(rs,row)->new Movimentacao(rs.getLong("id"),equipamentos.buscarPorId(rs.getLong("equipamento_id")).orElseThrow(),
                localizacoes.buscarPorId(rs.getLong("localizacao_origem_id")).orElseThrow(),localizacoes.buscarPorId(rs.getLong("localizacao_destino_id")).orElseThrow(),
                rs.getTimestamp("data_hora").toLocalDateTime(),rs.getString("observacao")),args);
    }
}
