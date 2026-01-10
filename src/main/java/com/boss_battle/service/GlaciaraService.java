package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossGlaciara;
import com.boss_battle.repository.GlaciaraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GlaciaraService {

    @Autowired
    private GlaciaraRepository repo;

    public GlobalBossGlaciara get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossGlaciara createDefaultBoss() {
    	GlobalBossGlaciara boss = new GlobalBossGlaciara();
         boss.setName("GLACIARA - RAINHA DO GELO");
         boss.setMaxHp(65_000L);
         boss.setCurrentHp(65_000L);
         boss.setProcessingDeath(false);
         boss.setAlive(true);
         boss.setImageUrl("images/boss_glaciara.webp");
         boss.setSpawnedAt(LocalDateTime.now());
         boss.setRespawnCooldownSeconds(6000L);
         boss.setSpawnCount(1);
         boss.setRewardBoss(60_000L);
         boss.setRewardExp(2400L);
        return repo.save(boss);
    }

    public GlobalBossGlaciara save(GlobalBossGlaciara boss) {
        return repo.save(boss);
    }
    
    public GlobalBossGlaciara attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }

   

}
