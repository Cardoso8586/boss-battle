package com.boss_battle.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.BossDamageLogDTO;
import com.boss_battle.service.BossDamageLogService;

@RestController
@RequestMapping("/api/boss")
public class BossDamageLogController {

    @Autowired
    private BossDamageLogService service;

    @GetMapping("/ranking/dano/{userId}")
    public Map<String, Object> ranking(@PathVariable Long userId) {

        return Map.of(
            "top10", service.top10Dano(),
            "minhaPosicao", service.posicaoUsuario(userId)
        );
    }


    // GET /api/boss/ataques-recentes
    @GetMapping("/ataques-recentes")
    public List<BossDamageLogDTO> ataquesRecentes() {
        return service.ultimosAtaques(10);
    }
    
    
}

