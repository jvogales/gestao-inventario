package com.jvogales.inventario.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;

final class JdbcKeys {
    private JdbcKeys() {}

    static long insert(JdbcTemplate jdbc, String sql, Object... valores) {
        var chave = new GeneratedKeyHolder();
        jdbc.update(conexao -> {
            PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < valores.length; i++) ps.setObject(i + 1, valores[i]);
            return ps;
        }, chave);
        var chaves = chave.getKeys();
        if (chaves != null) {
            Object id = chaves.get("id");
            if (id == null) id = chaves.get("ID");
            if (id instanceof Number numero) return numero.longValue();
        }
        if (chave.getKeyList().size() == 1 && !chave.getKeyList().get(0).isEmpty()) {
            Object primeira = chave.getKeyList().get(0).values().iterator().next();
            if (primeira instanceof Number numero) return numero.longValue();
        }
        throw new IllegalStateException("O banco não retornou o ID gerado.");
    }
}
