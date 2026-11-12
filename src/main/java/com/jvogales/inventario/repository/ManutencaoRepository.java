package com.jvogales.inventario.repository;

import com.jvogales.inventario.model.entity.Manutencao;
import com.jvogales.inventario.model.enums.StatusManutencao;
import com.jvogales.inventario.model.enums.TipoManutencao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository @RequiredArgsConstructor
public class ManutencaoRepository {
    private final JdbcTemplate jdbc;
    private final EquipamentoRepository equipamentos;
    public Manutencao salvar(Manutencao m) {
        if(m.getId()==null){ Long id=JdbcKeys.insert(jdbc,"INSERT INTO manutencao (equipamento_id,tipo,data_inicio,data_fim,descricao,status,observacao) VALUES (?,?,?,?,?,?,?)",
                m.getEquipamento().getId(),m.getTipo().name(),Date.valueOf(m.getDataInicio()),m.getDataFim()==null?null:Date.valueOf(m.getDataFim()),m.getDescricao(),m.getStatus().name(),m.getObservacao()); m.setId(id); }
        else jdbc.update("UPDATE manutencao SET tipo=?,data_inicio=?,data_fim=?,descricao=?,status=?,observacao=? WHERE id=?",m.getTipo().name(),Date.valueOf(m.getDataInicio()),m.getDataFim()==null?null:Date.valueOf(m.getDataFim()),m.getDescricao(),m.getStatus().name(),m.getObservacao(),m.getId());
        return m;
    }
    public Optional<Manutencao> buscarPorId(Long id){return listar(null).stream().filter(m->m.getId().equals(id)).findFirst();}
    public List<Manutencao> listar(Long equipamentoId){String sql="SELECT * FROM manutencao"+(equipamentoId==null?"":" WHERE equipamento_id=?")+" ORDER BY data_inicio DESC,id DESC"; Object[] args=equipamentoId==null?new Object[0]:new Object[]{equipamentoId}; return jdbc.query(sql,(rs,row)->new Manutencao(rs.getLong("id"),equipamentos.buscarPorId(rs.getLong("equipamento_id")).orElseThrow(),TipoManutencao.valueOf(rs.getString("tipo")),rs.getDate("data_inicio").toLocalDate(),rs.getDate("data_fim")==null?null:rs.getDate("data_fim").toLocalDate(),rs.getString("descricao"),StatusManutencao.valueOf(rs.getString("status")),rs.getString("observacao")),args);}
}
