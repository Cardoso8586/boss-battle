package com.boss_battle.controller.cacador_reconpensas_controller;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.cacador_reconpensas.masmorra_sombria.MasmorraSombriaService;

import jakarta.servlet.http.HttpSession;


@Controller
public class MasmorraSombriaController {

	  private final UsuarioBossBattleRepository usuarioRepository;
    private final MasmorraSombriaService masmorraSombriaService;

    public MasmorraSombriaController(
            MasmorraSombriaService masmorraSombriaService,
            UsuarioBossBattleRepository usuarioRepository
    ) {
        this.masmorraSombriaService = masmorraSombriaService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/masmorra-sombria")
    public String paginaMasmorraSombria(HttpSession session, Model model) {

        UsuarioBossBattle usuarioSessao =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuarioSessao == null) {
            return "redirect:/arena";
        }

        UsuarioBossBattle usuario = usuarioRepository.findById(usuarioSessao.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        
              /*
         * Proteção:
         * Se o usuário NÃO está em combate,
         * mas ainda existe inimigo salvo,
         * significa que o estado da masmorra ficou inconsistente.
         */
        
        if (usuario.getMasmorraCooldownAte() != null
                && LocalDateTime.now().isBefore(usuario.getMasmorraCooldownAte())) {

            return "redirect:/cacador-recompensas";
        }
  
        if (!Boolean.TRUE.equals(usuario.getMasmorraEmCombate())
                && usuario.getMasmorraInimigoAtual() != null) {

            return "redirect:/cacador-recompensas";
        }

        session.setAttribute("usuarioLogado", usuario);

        DecimalFormat df = new DecimalFormat("#,##0");

        model.addAttribute("usuario", usuario);
        model.addAttribute("idUsuario", usuario.getId());
        model.addAttribute("boss_coins", df.format(usuario.getBossCoins()));
        model.addAttribute("vigorAtual", usuario.getEnergiaGuerreiros());
        model.addAttribute("ataqueBase", masmorraSombriaService.calcularAtaqueBaseTotal(usuario));

        return "masmorra-sombria";
    }
    
    
    @PostMapping("/masmorra-sombria/entrar")
    @ResponseBody
    public Object entrar(HttpSession session) {

        UsuarioBossBattle usuario =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return java.util.Map.of(
                    "sucesso", false,
                    "status", "SESSAO_EXPIRADA",
                    "mensagem", "Sessão expirada."
            );
        }

        return masmorraSombriaService
                .entrarMasmorra(usuario.getId());
    }

    @PostMapping("/masmorra-sombria/atacar")
    @ResponseBody
    public Object atacar(HttpSession session) {

        UsuarioBossBattle usuario =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return java.util.Map.of(
                    "sucesso", false,
                    "status", "SESSAO_EXPIRADA",
                    "mensagem", "Sessão expirada."
            );
        }

        return masmorraSombriaService
                .atacarMasmorra(usuario.getId());
    }

    @GetMapping("/masmorra-sombria/status")
    @ResponseBody
    public Object status(HttpSession session) {

        UsuarioBossBattle usuario =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return java.util.Map.of(
                    "sucesso", false,
                    "status", "SESSAO_EXPIRADA",
                    "mensagem", "Sessão expirada."
            );
        }

        return masmorraSombriaService
                .statusMasmorra(usuario.getId());
    }
    
    @PostMapping("/masmorra-sombria/fugir")
    @ResponseBody
    public Object fugir(HttpSession session) {

        UsuarioBossBattle usuario =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return java.util.Map.of(
                    "sucesso", false,
                    "status", "SESSAO_EXPIRADA",
                    "mensagem", "Sessão expirada."
            );
        }

        return masmorraSombriaService.fugirMasmorra(usuario.getId());
    }
    
    @PostMapping("/masmorra-sombria/inimigo-atacar")
    @ResponseBody
    public Object inimigoAtacar(HttpSession session) {

        UsuarioBossBattle usuario =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return java.util.Map.of(
                    "sucesso", false,
                    "status", "SESSAO_EXPIRADA",
                    "mensagem", "Sessão expirada."
            );
        }

        return masmorraSombriaService.inimigoAtacarMasmorra(usuario.getId());
    }
    
    @PostMapping("/masmorra-sombria/usar-pocao")
    @ResponseBody
    public Map<String, Object> usarPocao(HttpSession session) {

        UsuarioBossBattle usuario =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return Map.of(
                    "sucesso", false,
                    "mensagem", "Usuário não logado."
            );
        }

        return masmorraSombriaService
                .usarPocao(usuario.getId());
    }
}