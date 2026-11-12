package com.jvogales.inventario.controller;

import com.jvogales.inventario.dto.BaixaRequest;
import com.jvogales.inventario.dto.EquipamentoRequest;
import com.jvogales.inventario.dto.ManutencaoRequest;
import com.jvogales.inventario.dto.MovimentacaoRequest;
import com.jvogales.inventario.model.entity.Categoria;
import com.jvogales.inventario.model.entity.Localizacao;
import com.jvogales.inventario.model.enums.EstadoEquipamento;
import com.jvogales.inventario.model.enums.StatusManutencao;
import com.jvogales.inventario.model.enums.TipoManutencao;
import com.jvogales.inventario.service.CategoriaService;
import com.jvogales.inventario.service.EquipamentoService;
import com.jvogales.inventario.service.HistoricoService;
import com.jvogales.inventario.service.LocalizacaoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class InventarioWebController {

    private final EquipamentoService equipamentos;
    private final CategoriaService categorias;
    private final LocalizacaoService locais;
    private final HistoricoService historico;

    @ModelAttribute("estadosAtivos")
    List<EstadoEquipamento> estadosAtivos() {
        return Arrays.stream(EstadoEquipamento.values()).filter(e -> e != EstadoEquipamento.BAIXADO).toList();
    }

    @GetMapping("/")
    String inventario(@RequestParam(required = false) String pesquisa, @RequestParam(required = false) Long categoriaId,
                      @RequestParam(required = false) Long localizacaoId,
                      @RequestParam(required = false) EstadoEquipamento estado, Model model) {
        catalogos(model);
        model.addAttribute("equipamentos", equipamentos.listar(pesquisa, categoriaId, localizacaoId, estado));
        model.addAttribute("pesquisa", pesquisa);
        model.addAttribute("categoriaId", categoriaId);
        model.addAttribute("localizacaoId", localizacaoId);
        model.addAttribute("estado", estado);
        model.addAttribute("estados", EstadoEquipamento.values());
        return "inventario";
    }

    @GetMapping("/inventario/{id}")
    String detalhe(@PathVariable Long id, Model model) {
        model.addAttribute("equipamento", equipamentos.buscarPorId(id));
        model.addAttribute("movimentacoes", historico.movimentacoes(id));
        model.addAttribute("manutencoes", historico.manutencoes(id));
        model.addAttribute("baixa", historico.buscarBaixa(id).orElse(null));
        return "detalhe";
    }

    @GetMapping({"/inventario/novo", "/inventario/{id}/editar"})
    String formularioEquipamento(@PathVariable(required = false) Long id, Model model) {
        var dados = new EquipamentoRequest();
        dados.setEstado(EstadoEquipamento.DISPONIVEL);
        if (id != null) {
            var atual = equipamentos.buscarPorId(id);
            dados.setNumeroPatrimonio(atual.getNumeroPatrimonio());
            dados.setIdentificacao(atual.getIdentificacao());
            dados.setMarca(atual.getMarca());
            dados.setModelo(atual.getModelo());
            dados.setObservacao(atual.getObservacao());
            dados.setEstado(atual.getEstado());
            dados.setCategoriaId(atual.getCategoria().getId());
            dados.setLocalizacaoId(atual.getLocalizacaoAtual().getId());
        }
        model.addAttribute("dados", dados);
        return prepararEquipamento(id, model);
    }

    @PostMapping({"/inventario/novo", "/inventario/{id}/editar"})
    String salvarEquipamento(@PathVariable(required = false) Long id, @Valid @ModelAttribute("dados") EquipamentoRequest dados,
                             BindingResult erros, Model model, RedirectAttributes redirect) {
        if (!erros.hasErrors()) {
            try {
                var salvo = id == null ? equipamentos.cadastrar(dados) : equipamentos.atualizar(id, dados);
                redirect.addFlashAttribute("mensagem", "Equipamento salvo.");
                return voltarAoDetalhe(salvo.getId());
            } catch (ResponseStatusException ex) {
                erros.reject("regra", ex.getReason());
            }
        }
        return prepararEquipamento(id, model);
    }

    @PostMapping("/inventario/{id}/estado")
    String alterarEstado(@PathVariable Long id, @RequestParam EstadoEquipamento estado, RedirectAttributes redirect) {
        equipamentos.alterarEstado(id, estado);
        redirect.addFlashAttribute("mensagem", "Estado atualizado.");
        return voltarAoDetalhe(id);
    }

    @GetMapping("/inventario/{id}/movimentar")
    String formularioMovimentacao(@PathVariable Long id, Model model) {
        model.addAttribute("dados", new MovimentacaoRequest(null, null));
        return prepararMovimentacao(id, model);
    }

    @PostMapping("/inventario/{id}/movimentar")
    String movimentar(@PathVariable Long id, @Valid @ModelAttribute("dados") MovimentacaoRequest dados,
                       BindingResult erros, Model model, RedirectAttributes redirect) {
        if (executar(erros, () -> historico.movimentar(id, dados))) {
            redirect.addFlashAttribute("mensagem", "Movimentação registrada.");
            return voltarAoDetalhe(id);
        }
        return prepararMovimentacao(id, model);
    }

    @GetMapping({"/inventario/{id}/manutencoes/nova", "/inventario/{id}/manutencoes/{manutencaoId}/editar"})
    String formularioManutencao(@PathVariable Long id, @PathVariable(required = false) Long manutencaoId, Model model) {
        var dados = new ManutencaoRequest(null, null, null, null, StatusManutencao.ABERTA, null);
        if (manutencaoId != null) {
            var atual = historico.buscarManutencao(id, manutencaoId);
            dados = new ManutencaoRequest(atual.tipo(), atual.dataInicio(), atual.dataFim(), atual.descricao(),
                    atual.status(), atual.observacao());
        }
        model.addAttribute("dados", dados);
        return prepararManutencao(id, manutencaoId, model);
    }

    @PostMapping({"/inventario/{id}/manutencoes/nova", "/inventario/{id}/manutencoes/{manutencaoId}/editar"})
    String salvarManutencao(@PathVariable Long id, @PathVariable(required = false) Long manutencaoId,
                           @Valid @ModelAttribute("dados") ManutencaoRequest dados, BindingResult erros,
                           Model model, RedirectAttributes redirect) {
        if (executar(erros, () -> {
            if (manutencaoId == null) {
                historico.registrarManutencao(id, dados);
            } else {
                historico.atualizarManutencao(id, manutencaoId, dados);
            }
        })) {
            redirect.addFlashAttribute("mensagem", "Manutenção salva.");
            return voltarAoDetalhe(id);
        }
        return prepararManutencao(id, manutencaoId, model);
    }

    @GetMapping("/inventario/{id}/baixar")
    String formularioBaixa(@PathVariable Long id, Model model) {
        model.addAttribute("dados", new BaixaRequest(null, null, null));
        return prepararBaixa(id, model);
    }

    @PostMapping("/inventario/{id}/baixar")
    String baixar(@PathVariable Long id, @Valid @ModelAttribute("dados") BaixaRequest dados,
                  BindingResult erros, Model model, RedirectAttributes redirect) {
        if (executar(erros, () -> historico.baixar(id, dados))) {
            redirect.addFlashAttribute("mensagem", "Baixa registrada.");
            return voltarAoDetalhe(id);
        }
        return prepararBaixa(id, model);
    }

    @GetMapping({"/cadastros/categorias", "/cadastros/categorias/{id}"})
    String categorias(@PathVariable(required = false) Long id, Model model) {
        model.addAttribute("dados", id == null ? new Categoria() : categorias.buscarPorId(id));
        return prepararCategoria(id, model);
    }

    @PostMapping({"/cadastros/categorias", "/cadastros/categorias/{id}"})
    String salvarCategoria(@PathVariable(required = false) Long id, @Valid @ModelAttribute("dados") Categoria dados,
                           BindingResult erros, Model model, RedirectAttributes redirect) {
        if (executar(erros, () -> {
            if (id == null) {
                categorias.cadastrar(dados);
            } else {
                categorias.atualizar(id, dados);
            }
        })) {
            redirect.addFlashAttribute("mensagem", "Categoria salva.");
            return "redirect:/cadastros/categorias";
        }
        return prepararCategoria(id, model);
    }

    @GetMapping({"/cadastros/localizacoes", "/cadastros/localizacoes/{id}"})
    String localizacoes(@PathVariable(required = false) Long id, Model model) {
        model.addAttribute("dados", id == null ? new Localizacao() : locais.buscarPorId(id));
        return prepararLocalizacao(id, model);
    }

    @PostMapping({"/cadastros/localizacoes", "/cadastros/localizacoes/{id}"})
    String salvarLocalizacao(@PathVariable(required = false) Long id,
                             @Valid @ModelAttribute("dados") Localizacao dados, BindingResult erros,
                             Model model, RedirectAttributes redirect) {
        if (executar(erros, () -> {
            if (id == null) {
                locais.cadastrar(dados);
            } else {
                locais.atualizar(id, dados);
            }
        })) {
            redirect.addFlashAttribute("mensagem", "Localização salva.");
            return "redirect:/cadastros/localizacoes";
        }
        return prepararLocalizacao(id, model);
    }

    @ExceptionHandler(ResponseStatusException.class)
    String exibirErro(ResponseStatusException ex, HttpServletResponse resposta, Model model) {
        resposta.setStatus(ex.getStatusCode().value());
        model.addAttribute("erro", ex.getReason());
        return "erro-inventario";
    }

    private void catalogos(Model model) {
        model.addAttribute("categorias", categorias.listar());
        model.addAttribute("locais", locais.listar());
    }

    private String prepararEquipamento(Long id, Model model) {
        catalogos(model);
        model.addAttribute("id", id);
        if (id != null) {
            model.addAttribute("equipamento", equipamentos.buscarPorId(id));
        }
        return "equipamento-form";
    }

    private String prepararMovimentacao(Long id, Model model) {
        var equipamento = equipamentos.buscarPorId(id);
        model.addAttribute("equipamento", equipamento);
        model.addAttribute("destinos", locais.listar().stream()
                .filter(local -> !local.getId().equals(equipamento.getLocalizacaoAtual().getId())).toList());
        return "movimentacao-form";
    }

    private String prepararManutencao(Long id, Long manutencaoId, Model model) {
        model.addAttribute("equipamento", equipamentos.buscarPorId(id));
        model.addAttribute("manutencaoId", manutencaoId);
        model.addAttribute("tipos", TipoManutencao.values());
        model.addAttribute("statusDisponiveis", StatusManutencao.values());
        return "manutencao-form";
    }

    private String prepararBaixa(Long id, Model model) {
        model.addAttribute("equipamento", equipamentos.buscarPorId(id));
        return "baixa-form";
    }

    private String prepararCategoria(Long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("categorias", categorias.listar());
        return "categorias-web";
    }

    private String prepararLocalizacao(Long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("locais", locais.listar());
        return "localizacoes-web";
    }

    private boolean executar(BindingResult erros, Runnable acao) {
        if (erros.hasErrors()) {
            return false;
        }
        try {
            acao.run();
            return true;
        } catch (ResponseStatusException ex) {
            erros.reject("regra", ex.getReason());
            return false;
        }
    }

    private String voltarAoDetalhe(Long id) {
        return "redirect:/inventario/" + id;
    }
}
