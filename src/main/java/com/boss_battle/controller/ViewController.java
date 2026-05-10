package com.boss_battle.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViewController {


    private final UsuarioBossBattleRepository usuarioRepository;

    public ViewController(UsuarioBossBattleRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


	
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // corresponde a templates/login.html
    }

    @GetMapping("/cadastro")
    public String registerPage() {
        return "cadastro"; // templates/cadastro.html
    }
    
    /*
    @GetMapping("/anuncio-recompensa")
    public String anuncioRecompensa() {
        return "anuncio-recompensa"; // templates/anuncio-recompensa.html
    }
    */
    
    @GetMapping("/anuncio-recompensa")
    public String anuncioRecompensa(@RequestParam("usuarioId") Long usuarioId,
                                    Model model) {

        UsuarioBossBattle usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        model.addAttribute("usuario", usuario);

        return "anuncio-recompensa";
    }
    

    @GetMapping("/desafios")
    public String desafiosArena(HttpSession session, Model model) {

        UsuarioBossBattle usuario =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/login";
        }

        DecimalFormat df = new DecimalFormat("#,##0");

        model.addAttribute("usuario", usuario);

        model.addAttribute("idUsuario", usuario.getId());

        model.addAttribute("xpUsuario",
                df.format(usuario.getExp()));

        model.addAttribute("nivelUsuario",
                df.format(usuario.getNivel()));

        BigDecimal coins = usuario.getBossCoins();

        if (coins == null) {
            coins = BigDecimal.ZERO;
        }

        model.addAttribute("boss_coins",
                df.format(coins));

        return "desafios";
    }

    @GetMapping("/aliados")
    public String mostrarCadastro(@RequestParam(required = false) String ref, Model model) {
        // Cria um objeto usuário vazio para o formulário
        model.addAttribute("usuario", new UsuarioBossBattle());
        // Passa a referência (ou string vazia) para o formulário
        model.addAttribute("ref", ref != null ? ref : "");
        return "cadastro"; // nome do template Thymeleaf
    }



    
}
//