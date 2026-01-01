package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossInfernax;
import com.boss_battle.repository.InfernaxRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class InfernaxService {

    @Autowired
    private InfernaxRepository repo;

    public GlobalBossInfernax get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossInfernax createDefaultBoss() {
    	GlobalBossInfernax boss = new GlobalBossInfernax();
        boss.setName("INFERNAX");
        boss.setMaxHp(320_000L);
        boss.setCurrentHp(320_000L);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_infernax.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(7200); 
        boss.setSpawnCount(1);
        boss.setRewardBoss(180_000L);
        boss.setRewardExp(4200);
        return repo.save(boss);
    }

    public GlobalBossInfernax save(GlobalBossInfernax boss) {
        return repo.save(boss);
    }
    
    public GlobalBossInfernax attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }
}
