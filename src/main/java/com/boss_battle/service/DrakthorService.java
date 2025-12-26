package com.boss_battle.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossDrakthor;
import com.boss_battle.repository.DrakthorRepository;

@Service
public class DrakthorService {

    @Autowired
    private DrakthorRepository repo;

    public GlobalBossDrakthor get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    
    public GlobalBossDrakthor createDefaultBoss() {
        GlobalBossDrakthor boss = new GlobalBossDrakthor();
        boss.setName("DRAKTHOR — O DEVORA-MUNDOS");
        boss.setMaxHp(150_000);
        boss.setCurrentHp(150_000);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_drakthor.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(7200);
        boss.setSpawnCount(1);
        boss.setRewardBoss(35_000); 
        boss.setRewardExp(1500);

        return repo.save(boss);
    }

    public GlobalBossDrakthor save(GlobalBossDrakthor boss) {
        return repo.save(boss);
    }

    public GlobalBossDrakthor attack(long damage) {
        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}
