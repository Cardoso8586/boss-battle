package com.boss_battle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boss_battle.model.UsuarioBossBattle;

@Controller
public class ViewController {



	
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // corresponde a templates/login.html
    }

    @GetMapping("/cadastro")
    public String registerPage() {
        return "cadastro"; // templates/cadastro.html
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