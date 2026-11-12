package com.jvogales.inventario.controller;

import com.jvogales.inventario.dto.BaixaRequest;
import com.jvogales.inventario.dto.EquipamentoRequest;
import com.jvogales.inventario.dto.ManutencaoRequest;
import com.jvogales.inventario.dto.MovimentacaoRequest;
import com.jvogales.inventario.model.entity.Categoria;
import com.jvogales.inventario.model.entity.Localizacao;
import com.jvogales.inventario.model.enums.EstadoEquipamento;
import com.jvogales.inventario.model.enums.TipoManutencao;
import com.jvogales.inventario.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final CategoriaService categorias;
    private final LocalizacaoService localizacoes;
    private final EquipamentoService equipamentos;
    private final MovimentacaoService movimentacoes;
    private final ManutencaoService manutencoes;
    private final BaixaService baixas;

    @GetMapping({"/", "/app"})
    public String painel(Model model) {
        var listaEquipamentos = equipamentos.listar(null, null, null, null);
        model.addAttribute("categorias", categorias.listar());
        model.addAttribute("localizacoes", localizacoes.listar());
        model.addAttribute("equipamentos", listaEquipamentos);
        model.addAttribute("movimentacoes", movimentacoes.listar(null));
        model.addAttribute("manutencoes", manutencoes.listar(null));
        model.addAttribute("baixas", baixas.listar());
        model.addAttribute("estados", EstadoEquipamento.values());
        model.addAttribute("tiposManutencao", TipoManutencao.values());
        model.addAttribute("totalAtivos", listaEquipamentos.stream().filter(e -> e.getEstado() != EstadoEquipamento.BAIXADO).count());
        model.addAttribute("totalManutencao", listaEquipamentos.stream().filter(e -> e.getEstado() == EstadoEquipamento.EM_MANUTENCAO).count());
        return "painel";
    }

    @PostMapping("/app/categorias")
    public String categoria(@RequestParam String nome, @RequestParam(required=false) String descricao, RedirectAttributes flash) {
        return executar(() -> categorias.cadastrar(new Categoria(null, nome, descricao)), "Categoria cadastrada.", "categorias", flash);
    }
    @PostMapping("/app/localizacoes")
    public String localizacao(@RequestParam String bloco, @RequestParam String local, @RequestParam(required=false) String descricao, RedirectAttributes flash) {
        return executar(() -> localizacoes.cadastrar(new Localizacao(null, bloco, local, descricao)), "Localização cadastrada.", "localizacoes", flash);
    }
    @PostMapping("/app/equipamentos")
    public String equipamento(@ModelAttribute EquipamentoRequest dados, RedirectAttributes flash) {
        return executar(() -> equipamentos.cadastrar(dados), "Equipamento cadastrado.", "equipamentos", flash);
    }
    @PostMapping("/app/movimentacoes")
    public String movimentacao(@ModelAttribute MovimentacaoRequest dados, RedirectAttributes flash) {
        return executar(() -> movimentacoes.registrar(dados), "Movimentação registrada.", "movimentacoes", flash);
    }
    @PostMapping("/app/manutencoes")
    public String manutencao(@ModelAttribute ManutencaoRequest dados, RedirectAttributes flash) {
        return executar(() -> manutencoes.registrar(dados), "Manutenção registrada.", "manutencoes", flash);
    }
    @PostMapping("/app/manutencoes/{id}/concluir")
    public String concluir(@PathVariable Long id, RedirectAttributes flash) {
        return executar(() -> manutencoes.concluir(id), "Manutenção concluída.", "manutencoes", flash);
    }
    @PostMapping("/app/baixas")
    public String baixa(@ModelAttribute BaixaRequest dados, RedirectAttributes flash) {
        return executar(() -> baixas.registrar(dados), "Baixa registrada sem apagar o histórico.", "baixas", flash);
    }

    private String executar(Runnable acao, String sucesso, String ancora, RedirectAttributes flash) {
        try { acao.run(); flash.addFlashAttribute("sucesso", sucesso); }
        catch (Exception e) { flash.addFlashAttribute("erro", mensagem(e)); }
        return "redirect:/app#" + ancora;
    }
    private String mensagem(Exception e) {
        if (e instanceof org.springframework.web.server.ResponseStatusException r && r.getReason() != null) return r.getReason();
        if (e instanceof org.springframework.dao.DataIntegrityViolationException) return "Dados duplicados ou vinculados impedem esta operação.";
        return "Não foi possível concluir a operação. Confira os campos e tente novamente.";
    }
}
