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

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;


@Controller
public class ReferidosRecompensaController {

    @Autowired
    private ReferidosRecompensaService recompensaService;

    // Endpoint para o usuário "claimar" os ganhos acumulados
    @PostMapping("/claim-referidos")
    public ResponseEntity<?> claimGanhos(HttpSession session) {

        UsuarioBossBattle usuario =
                (UsuarioBossBattle) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }

        recompensaService.claimGanhos(usuario.getId());

        return ResponseEntity.ok().build();
    }
}
