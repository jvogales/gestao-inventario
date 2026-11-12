package com.jvogales.inventario.repository;

import com.jvogales.inventario.model.entity.Baixa;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository @RequiredArgsConstructor
public class BaixaRepository {
    private final JdbcTemplate jdbc;
    private final EquipamentoRepository equipamentos;
    public Baixa salvar(Baixa b){Long id=JdbcKeys.insert(jdbc,"INSERT INTO baixa (equipamento_id,data,motivo,observacao) VALUES (?,?,?,?)",b.getEquipamento().getId(),Date.valueOf(b.getData()),b.getMotivo(),b.getObservacao());b.setId(id);return b;}
    public Optional<Baixa> buscarPorEquipamento(Long id){return listar().stream().filter(b->b.getEquipamento().getId().equals(id)).findFirst();}
    public List<Baixa> listar(){return jdbc.query("SELECT * FROM baixa ORDER BY data DESC,id DESC",(rs,row)->new Baixa(rs.getLong("id"),equipamentos.buscarPorId(rs.getLong("equipamento_id")).orElseThrow(),rs.getDate("data").toLocalDate(),rs.getString("motivo"),rs.getString("observacao")));}
}
