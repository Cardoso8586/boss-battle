package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossMorvath;
import com.boss_battle.repository.MorvathRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MorvathService {

    @Autowired
    private MorvathRepository repo;

    public GlobalBossMorvath get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossMorvath createDefaultBoss() {
        GlobalBossMorvath boss = new GlobalBossMorvath();
        boss.setName("MORVATH");
        boss.setMaxHp(50_000L);
        boss.setCurrentHp(50_000L);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_morvath.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(5400); // 90 minutos
        boss.setSpawnCount(1);
        boss.setRewardBoss(50_000L);
        boss.setRewardExp(2200);
        return repo.save(boss);
    }

    public GlobalBossMorvath save(GlobalBossMorvath boss) {
        return repo.save(boss);
    }
    
    public GlobalBossMorvath attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }
}
