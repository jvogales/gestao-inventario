package com.jvogales.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
       
class InventarioApiTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void cadastraEditaERegistraBaixaPelaRotaLegada() throws Exception {
        long categoria = criar("/categorias", Map.of("id", 9000, "nome", "Informática"));
        long local = criar("/localizacoes", Map.of("bloco", "A", "local", "Laboratório"));
        assertThat(categoria).isNotEqualTo(9000L);
        var dados = equipamento(categoria, local);
        long id = criar("/equipamentos", dados);

        mvc.perform(get("/equipamentos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria.id").value(categoria))
                .andExpect(jsonPath("$.localizacaoAtual.id").value(local));
        for (String rota : new String[]{"/equipamentos", "/categorias", "/localizacoes"}) {
            mvc.perform(get(rota)).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
        }

        dados.put("identificacao", "Computador atualizado");
        mvc.perform(put("/equipamentos/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.identificacao").value("Computador atualizado"));
        mvc.perform(put("/categorias/{id}", categoria).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Computadores\"}"))
                .andExpect(status().isOk());
        mvc.perform(put("/localizacoes/{id}", local).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bloco\":\"B\",\"local\":\"Sala 2\"}"))
                .andExpect(status().isOk());
        mvc.perform(get("/equipamentos/{id}", id))
                .andExpect(jsonPath("$.categoria.nome").value("Computadores"))
                .andExpect(jsonPath("$.localizacaoAtual.bloco").value("B"));

        mvc.perform(delete("/categorias/{id}", categoria)).andExpect(status().isConflict());
        mvc.perform(delete("/localizacoes/{id}", local)).andExpect(status().isConflict());
        mvc.perform(delete("/equipamentos/{id}", id))
                .andExpect(status().isNoContent()).andExpect(content().string(""));
        mvc.perform(delete("/categorias/{id}", categoria)).andExpect(status().isConflict());
        mvc.perform(delete("/localizacoes/{id}", local)).andExpect(status().isConflict());
        mvc.perform(get("/equipamentos/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("BAIXADO"));
        mvc.perform(get("/equipamentos/{id}/baixa", id)).andExpect(status().isOk());
    }

    @Test
    void retorna400ParaDadosInvalidosE404ParaRecursosAusentes() throws Exception {
        mvc.perform(post("/equipamentos").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.numeroPatrimonio").exists())
                .andExpect(jsonPath("$.campos.categoriaId").exists());
        mvc.perform(post("/categorias").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"   \"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/localizacoes").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bloco\":\"A\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/equipamentos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\":\"INEXISTENTE\"}"))
                .andExpect(status().isBadRequest());
        for (String rota : new String[]{"/equipamentos", "/categorias", "/localizacoes"}) {
            mvc.perform(get(rota + "/999999")).andExpect(status().isNotFound());
            mvc.perform(delete(rota + "/999999")).andExpect(status().isNotFound());
        }
        var dados = equipamento(999999L, 999999L);
        mvc.perform(post("/equipamentos").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dados)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void rejeitaPatrimonioDuplicadoSemAlterarEquipamentoExistente() throws Exception {
        long categoria = criar("/categorias", Map.of("nome", "Duplicidade " + UUID.randomUUID()));
        long local = criar("/localizacoes", Map.of("bloco", "A", "local", "Sala"));
        var primeiro = equipamento(categoria, local);
        long id = criar("/equipamentos", primeiro);
        mvc.perform(post("/equipamentos").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(primeiro)))
                .andExpect(status().isConflict());

        var segundo = equipamento(categoria, local);
        long segundoId = criar("/equipamentos", segundo);
        mvc.perform(put("/equipamentos/{id}", segundoId).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(primeiro)))
                .andExpect(status().isConflict());
        mvc.perform(get("/equipamentos/{id}", segundoId))
                .andExpect(jsonPath("$.numeroPatrimonio").value(segundo.get("numeroPatrimonio")));
        mvc.perform(put("/equipamentos/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(primeiro)))
                .andExpect(status().isOk());
    }

    @Test
    void pesquisaECombinaFiltrosDeEquipamentos() throws Exception {
        long categoria = criar("/categorias", Map.of("nome", "Teste de filtro"));
        long local = criar("/localizacoes", Map.of("bloco", "B", "local", "Sala"));
        var dados = equipamento(categoria, local);
        long id = criar("/equipamentos", dados);
        mvc.perform(get("/equipamentos")
                        .param("pesquisa", dados.get("numeroPatrimonio").toString().toLowerCase())
                        .param("categoriaId", String.valueOf(categoria))
                        .param("localizacaoId", String.valueOf(local))
                        .param("estado", "DISPONIVEL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(id));
        mvc.perform(get("/equipamentos").param("categoriaId", String.valueOf(categoria))
                        .param("estado", "BAIXADO"))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void registraHistoricosEConservaEquipamentoAposBaixa() throws Exception {
        long categoria = criar("/categorias", Map.of("nome", "Equipamentos de laboratório"));
        long origem = criar("/localizacoes", Map.of("bloco", "A", "local", "Sala 1"));
        long destino = criar("/localizacoes", Map.of("bloco", "A", "local", "Sala 2"));
        long id = criar("/equipamentos", equipamento(categoria, origem));
        String rota = "/equipamentos/" + id;

        mvc.perform(post(rota + "/movimentacoes").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("destinoId", origem))))
                .andExpect(status().isConflict());
        mvc.perform(post(rota + "/movimentacoes").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("destinoId", destino, "observacao", "Transferência"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.origem.id").value(origem))
                .andExpect(jsonPath("$.destino.id").value(destino));
        mvc.perform(get(rota)).andExpect(jsonPath("$.localizacaoAtual.id").value(destino));
        mvc.perform(delete("/localizacoes/{id}", origem)).andExpect(status().isConflict());

        Map<String, Object> manutencao = new HashMap<>(Map.of("tipo", "PREVENTIVA", "dataInicio", "2026-10-20",
                "descricao", "Limpeza", "status", "ABERTA"));
        var resposta = mvc.perform(post(rota + "/manutencoes").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(manutencao)))
                .andExpect(status().isCreated()).andReturn().getResponse();
        long manutencaoId = mapper.readTree(resposta.getContentAsString()).get("id").asLong();
        manutencao.put("status", "CONCLUIDA");
        mvc.perform(put(rota + "/manutencoes/{id}", manutencaoId).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(manutencao)))
                .andExpect(status().isBadRequest());
        manutencao.put("dataFim", "2026-10-21");
        mvc.perform(put(rota + "/manutencoes/{id}", manutencaoId).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(manutencao)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CONCLUIDA"));

        String baixa = mapper.writeValueAsString(Map.of("data", "2026-10-22", "motivo", "Fim da vida útil"));
        mvc.perform(post(rota + "/baixa").contentType(MediaType.APPLICATION_JSON).content(baixa))
                .andExpect(status().isCreated());
        mvc.perform(post(rota + "/baixa").contentType(MediaType.APPLICATION_JSON).content(baixa))
                .andExpect(status().isConflict());
        mvc.perform(post(rota + "/movimentacoes").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("destinoId", origem))))
                .andExpect(status().isConflict());
        mvc.perform(get(rota)).andExpect(jsonPath("$.estado").value("BAIXADO"));
        mvc.perform(get(rota + "/movimentacoes")).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(get(rota + "/manutencoes")).andExpect(jsonPath("$[0].status").value("CONCLUIDA"));
        mvc.perform(get("/inventario/{id}", id)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Baixa patrimonial")));
    }

    @Test
    void renderizaFormulariosERegistraMovimentacaoPelaInterface() throws Exception {
        long categoria = criar("/categorias", Map.of("nome", "Categoria web"));
        long origem = criar("/localizacoes", Map.of("bloco", "A", "local", "Depósito"));
        long destino = criar("/localizacoes", Map.of("bloco", "B", "local", "Laboratório"));
        long id = criar("/equipamentos", equipamento(categoria, origem));
        String rota = "/inventario/" + id;

        for (String tela : new String[]{"/", "/inventario/novo", rota, rota + "/editar",
                rota + "/movimentar", rota + "/manutencoes/nova", rota + "/baixar",
                "/cadastros/categorias", "/cadastros/localizacoes"}) {
            mvc.perform(get(tela)).andExpect(status().isOk());
        }
        mvc.perform(post(rota + "/movimentar")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Informe o destino.")));
        mvc.perform(post(rota + "/movimentar").param("destinoId", String.valueOf(destino)))
                .andExpect(redirectedUrl(rota));
        mvc.perform(get("/equipamentos/{id}", id)).andExpect(jsonPath("$.localizacaoAtual.id").value(destino));
        mvc.perform(post(rota + "/manutencoes/nova").param("tipo", "CORRETIVA")
                        .param("status", "ABERTA").param("dataInicio", "2026-10-20").param("descricao", "Reparo"))
                .andExpect(redirectedUrl(rota));
    }
    private long criar(String rota, Map<String, ?> dados) throws Exception {
        var resposta = mvc.perform(post(rota).contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dados)))
                .andExpect(status().isCreated()).andReturn().getResponse();
        long id = mapper.readTree(resposta.getContentAsString()).get("id").asLong();
        assertThat(resposta.getHeader("Location")).endsWith(rota + "/" + id);
        return id;
    }

    private Map<String, Object> equipamento(long categoria, long local) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("numeroPatrimonio", "PAT-" + UUID.randomUUID());
        dados.put("identificacao", "Computador");
        dados.put("estado", "DISPONIVEL");
        dados.put("categoriaId", categoria);
        dados.put("localizacaoId", local);
        return dados;
    }
}
