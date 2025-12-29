package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossThunderon;
import com.boss_battle.repository.ThunderonRepository;

@Service
public class ThunderonService {

    @Autowired
    private ThunderonRepository repo;

    public GlobalBossThunderon get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossThunderon createDefaultBoss() {
    	GlobalBossThunderon boss = new GlobalBossThunderon();
        boss.setName("THUNDERON");
        boss.setMaxHp(260_000L);
        boss.setCurrentHp(260_000L);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_thunderon.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(6000L); 
        boss.setSpawnCount(1);
        boss.setRewardBoss(140_000L);
        boss.setRewardExp(3600);
        return repo.save(boss);
    }

    public GlobalBossThunderon save(GlobalBossThunderon boss) {
        return repo.save(boss);
    }
    
    public GlobalBossThunderon attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }
}
