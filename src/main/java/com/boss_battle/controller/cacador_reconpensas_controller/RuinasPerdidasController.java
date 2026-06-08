package com.boss_battle.controller.cacador_reconpensas_controller;

import java.text.DecimalFormat;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boss_battle.dto.cacador_reconpensasDTO.RuinasPerdidasResponse;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.cacador_reconpensas.ruinas_perdidas.RuinasPerdidasService;

@Controller
public class RuinasPerdidasController {

    private final RuinasPerdidasService ruinasPerdidasService;
    private final UsuarioBossBattleRepository usuarioRepository;

    public RuinasPerdidasController(
            RuinasPerdidasService ruinasPerdidasService,
            UsuarioBossBattleRepository usuarioRepository
    ) {
        this.ruinasPerdidasService = ruinasPerdidasService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/ruinas-perdidas")
    public String paginaRuinasPerdidas(HttpSession session, Model model) {

        UsuarioBossBattle usuarioSessao =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuarioSessao == null) {
            return "redirect:/arena";
        }

        UsuarioBossBattle usuario = usuarioRepository
                .findById(usuarioSessao.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        session.setAttribute("usuarioLogado", usuario);

        DecimalFormat df = new DecimalFormat("#,##0");

        int tentativasHoje =
                usuario.getTentativasRuinasHoje() == null
                        ? 0
                        : usuario.getTentativasRuinasHoje();

        model.addAttribute("usuario", usuario);
        model.addAttribute("idUsuario", usuario.getId());
        model.addAttribute("boss_coins", df.format(usuario.getBossCoins()));

        model.addAttribute("tentativasHoje", tentativasHoje);
        model.addAttribute("tentativasRestantes", 10 - tentativasHoje);

        return "ruinas-perdidas";
    }
    @PostMapping("/ruinas-perdidas/explorar")
    @ResponseBody
    public RuinasPerdidasResponse explorarRuinas(HttpSession session) {

        UsuarioBossBattle usuarioSessao =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuarioSessao == null) {

            return new RuinasPerdidasResponse(
                    false,
                    "Sessão expirada.",
                    null,
                    "SESSAO_EXPIRADA",
                    0L,
                    null,
                    0L,
                    0,
                    0L,
                    false
            );
        }

        return ruinasPerdidasService.explorarRuinas(usuarioSessao.getId());
    }
    
    @GetMapping("/ruinas-perdidas/status/{usuarioId}")
    @ResponseBody
    public Map<String, Object> statusRuinas(@PathVariable Long usuarioId) {
        return ruinasPerdidasService.verificarStatusRuinas(usuarioId);
    }
}