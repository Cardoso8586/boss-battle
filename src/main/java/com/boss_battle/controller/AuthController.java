package com.boss_battle.controller;


import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.CaptchaService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	 @Autowired
	UsuarioBossBattleRepository usuarioRepository;
	
	  @Autowired
	    private PasswordEncoder passwordEncoder;
	  
    @Autowired
	CaptchaService  captchaService;
	





    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarUsuario(@RequestBody Map<String, String> payload) {

        String username = payload.get("username");
        String email = payload.get("email");
        String senha = payload.get("senha");
        String captchaToken = payload.get("captcha");
        String ref = payload.get("ref");

        // Verifica CAPTCHA
        if (captchaToken == null || !captchaService.isValid(captchaToken)) {
            return ResponseEntity.badRequest().body("Falha na verificação do CAPTCHA.");
        }

        // Verifica se username ou email já existem
        if (usuarioRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Esse nome de guerra já está em uso.");
        }
        if (usuarioRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Este correio já foi registrado por outro guerreiro.");
        }

         // Cria usuário
        UsuarioBossBattle usuario = new UsuarioBossBattle();
        // Processa referência
        if (ref != null && !ref.isBlank()) {
            try {
                String decoded = new String(Base64.getUrlDecoder().decode(ref));
                Long referrerId = Long.parseLong(decoded);
                if (usuarioRepository.existsById(referrerId)) {
                	
                	
               usuario.setReferredBy(referrerId);
                

                System.out.println("[Cadastro] Usuário referido por: " + referrerId);
                }
            } catch (Exception e) {
                System.out.println("[Cadastro] Erro ao processar ref: " + e.getMessage());
            }
        }

      
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));

        usuarioRepository.save(usuario);

        return ResponseEntity.ok("SUCESSO");
    }

    //===================================================================================
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> payload, HttpSession session) {
        String emailOuUsuario = payload.get("loginUser");
        String senha = payload.get("loginSenha");

        UsuarioBossBattle usuario = usuarioRepository.findByEmailOrUsername(emailOuUsuario, emailOuUsuario);

        if (usuario != null && passwordEncoder.matches(senha, usuario.getSenha())) {
            // Salva na sessão
            session.setAttribute("usuarioLogado", usuario);
            return ResponseEntity.ok("Login realizado com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Email/Usuário ou senha inválidos!");
        }
    }

    

}
