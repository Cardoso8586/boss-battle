package com.boss_battle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;

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