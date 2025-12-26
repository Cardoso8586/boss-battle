package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossNoctyr;
import com.boss_battle.repository.NoctyrRepository;

@Service
public class NoctyrService {

    @Autowired
    private NoctyrRepository repo;

    public GlobalBossNoctyr get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    
    public GlobalBossNoctyr createDefaultBoss() {
    	GlobalBossNoctyr boss = new GlobalBossNoctyr();
        boss.setName("NOCTYR");
        boss.setMaxHp(60_000);
        boss.setCurrentHp(60_000);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_noctyr.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(5400);
        boss.setSpawnCount(1);
        boss.setRewardBoss(55_000); 
        boss.setRewardExp(2200);

        return repo.save(boss);
    }

    public GlobalBossNoctyr save(GlobalBossNoctyr boss) {
        return repo.save(boss);
    }

    public GlobalBossNoctyr attack(long damage) {
        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}