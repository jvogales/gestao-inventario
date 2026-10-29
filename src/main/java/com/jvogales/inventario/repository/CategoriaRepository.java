package com.jvogales.inventario.repository;

import com.jvogales.inventario.model.entity.Categoria;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoriaRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Categoria> rowMapper = (rs, numeroLinha) ->
            new Categoria(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("descricao")
            );

    public List<Categoria> listar() {
        String sql = "SELECT id, nome, descricao FROM categoria ORDER BY id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Categoria> buscarPorId(Long id) {
        String sql = "SELECT id, nome, descricao FROM categoria WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst();
    }

    public Categoria salvar(Categoria categoria) {
        try {
            if (categoria.getId() == null) {
                String sql = """
                        INSERT INTO categoria (nome, descricao)
                        VALUES (?, ?)
                        RETURNING id
                        """;

                Long id = jdbcTemplate.queryForObject(
                        sql,
                        Long.class,
                        categoria.getNome(),
                        categoria.getDescricao()
                );

                categoria.setId(id);
            } else {
                String sql = """
                        UPDATE categoria
                        SET nome = ?, descricao = ?
                        WHERE id = ?
                        """;

                jdbcTemplate.update(
                        sql,
                        categoria.getNome(),
                        categoria.getDescricao(),
                        categoria.getId()
                );
            }

            return categoria;

        } catch (DataIntegrityViolationException ex) {
            if (violouUnicidadeNome(ex)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Já existe uma categoria com esse nome.",
                        ex
                );
            }

            throw ex;
        }
    }

    public void excluir(Long id) {
        String sql = "DELETE FROM categoria WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    private boolean violouUnicidadeNome(DataIntegrityViolationException ex) {
        Throwable causa = ex;

        while (causa != null) {
            if (causa instanceof java.sql.SQLException erro) {
                if ("23505".equals(erro.getSQLState())
                        && erro.getMessage() != null
                        && erro.getMessage().contains("uq_categoria_nome_normalizado")) {
                    return true;
                }
            }

            causa = causa.getCause();
        }

        return false;
    }   
}
