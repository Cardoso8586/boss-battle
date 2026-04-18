package com.boss_battle.controller;



import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.ativar_equipar.EscudoPrimordialService;

@RestController
@RequestMapping("/api/escudo-primordial")
public class EscudoPrimordialController {

    @Autowired
    private EscudoPrimordialService escudoPrimordialService;

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    @PostMapping("/ativar")
    public Map<String, Object> ativarEscudoPrimordial(
            @RequestParam Long usuarioId,
            @RequestParam int quantidade
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            escudoPrimordialService.ativarEscudoPrimordial(usuarioId, quantidade);

            UsuarioBossBattle usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            response.put("success", true);
            response.put("message", "Escudo Primordial ativado com sucesso!");
            response.put("escudoPrimordial", usuario.getEscudoPrimordial());
            response.put("escudoPrimordialAtivo", usuario.getEscudoPrimordialAtivo());
            response.put("escudoPrimordialDesgaste", usuario.getEscudoPrimordialDesgaste());

        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }

    /*
    @GetMapping("/ativo")
    public Map<String, Object> verificarEscudoAtivo(@RequestParam Long usuarioId) {
        Map<String, Object> response = new HashMap<>();

        UsuarioBossBattle usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        boolean ativo = usuario.getEscudoPrimordialAtivo() > 0;
        response.put("success", true);
        response.put("escudoAtivo", ativo);
        response.put("quantidadeAtiva", usuario.getEscudoPrimordialAtivo());
        response.put("desgasteAtual", usuario.getEscudoPrimordialDesgaste());
        response.put("estoque", usuario.getEscudoPrimordial());

        return response;
    }
    
    */
}