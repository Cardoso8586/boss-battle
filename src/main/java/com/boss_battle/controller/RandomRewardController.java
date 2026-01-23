package com.boss_battle.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boss_battle.dto.RandomLevelRewardDTO;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.service.RandomRewardService;

@RestController
@RequestMapping("/api/rewards")
public class RandomRewardController {

    private final RandomRewardService rewardService;

    public RandomRewardController(RandomRewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/preview/{userId}")
    public RandomLevelRewardDTO preview(@PathVariable Long userId) {
        return RandomLevelRewardDTO.fromEntity(
            rewardService.getPreview(userId)
        );
    }


    // ⬆️ ENTREGA — CHAMAR QUANDO CONFIRMAR O LEVEL UP
    @PostMapping("/claim/{usuarioId}")
    public ResponseEntity<Void> claim(@PathVariable UsuarioBossBattle usuario) {

        rewardService.onLevelUp(usuario);

        return ResponseEntity.ok().build();
    }
}
