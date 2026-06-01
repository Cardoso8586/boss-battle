package com.boss_battle.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.boss_battle.dto.ClaimRequestDTO;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.ReferidosRecompensaService;

import org.springframework.security.core.Authentication;


@Controller
public class ReferidosRecompensaController {




    @Autowired
    private ReferidosRecompensaService recompensaService;

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;



    // Endpoint para o usuário "claimar" os ganhos acumulados
    @PostMapping("/claim-referidos")
    public ResponseEntity<?> claimGanhos(Authentication auth) {

        UsuarioBossBattle usuario = usuarioRepository
                .findByUsername(auth.getName())
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        recompensaService.claimGanhos(usuario.getId());

        return ResponseEntity.ok().build();
    }
}
