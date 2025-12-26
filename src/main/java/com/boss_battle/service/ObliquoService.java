package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossObliquo;
import com.boss_battle.repository.ObliquoRepository;

@Service
public class ObliquoService {

    @Autowired
    private ObliquoRepository repo;

    public GlobalBossObliquo get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossObliquo createDefaultBoss() {
        GlobalBossObliquo boss = new GlobalBossObliquo();
        boss.setName("OBLÍQUO");
        boss.setMaxHp(48_000L);
        boss.setCurrentHp(48_000L);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_obliquo.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(4800); // 80 minutos
        boss.setSpawnCount(1);
        boss.setRewardBoss(45_000L);
        boss.setRewardExp(2000);
        return repo.save(boss);
    }

    public GlobalBossObliquo save(GlobalBossObliquo boss) {
        return repo.save(boss);
    }
    
    public GlobalBossObliquo attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // atualiza no banco
    }
}
