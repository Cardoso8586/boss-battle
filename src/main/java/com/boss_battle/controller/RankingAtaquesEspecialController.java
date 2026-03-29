package com.boss_battle.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.UsuarioRankingAtaquesDTO;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.service.missoes.RankingAtaqueEspecialService;

@RestController
@RequestMapping("/api")
public class RankingAtaquesEspecialController {

    private final RankingAtaqueEspecialService rankingService;

    public RankingAtaquesEspecialController(RankingAtaqueEspecialService rankingService) {
        this.rankingService = rankingService;
    }

    // 1) Ranking de ataques especiais (top 10)
    @GetMapping("/ranking-ataques-especial")
    public List<UsuarioBossBattle> getRankingAtaquesEspecial() {
        return rankingService.getRankingSemanalTop(10);
    }

    // 2) Minha posição no ranking
    @GetMapping("/minha-posicao-ataques-especial")
    public ResponseEntity<UsuarioRankingAtaquesDTO> getMinhaPosicao(
            @RequestParam Long usuarioId) {
        UsuarioRankingAtaquesDTO posicao = rankingService.getMinhaPosicao(usuarioId);
        if (posicao == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(posicao);
    }

    // 3) Prêmio pendente
    @GetMapping("/usuario/{id}/premio-pendente-ataque-especial")
    public BigDecimal getPremioPendente(@PathVariable Long id) {
        return rankingService.getPremioPendente(id);
    }

    // 4) Confirmar prêmio
    @PostMapping("/usuario/{id}/confirmar-premio-ataque-especial")
    public ResponseEntity<String> confirmarPremio(@PathVariable Long id) {
        boolean confirmado = rankingService.confirmarPremio(id);
        if (confirmado) {
            return ResponseEntity.ok("Prêmio de ataque especial creditado com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Nenhum prêmio pendente de ataque especial.");
        }
    }
}