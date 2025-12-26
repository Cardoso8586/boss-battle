package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossIgnorath;
import com.boss_battle.repository.IgnorathRepository;

@Service
public class IgnorathService {

    @Autowired
    private IgnorathRepository repo;

    public GlobalBossIgnorath get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossIgnorath createDefaultBoss() {
    	 GlobalBossIgnorath boss = new GlobalBossIgnorath();
         boss.setName("IGNORATH");
         boss.setMaxHp(50_000);
         boss.setCurrentHp(50_000);
         boss.setAlive(true);
         boss.setImageUrl("images/boss_ignorath.webp");
         boss.setSpawnedAt(LocalDateTime.now());
         boss.setRespawnCooldownSeconds(3600);
         boss.setSpawnCount(1);
         boss.setRewardBoss(25_000);
         boss.setRewardExp(1000);
        return repo.save(boss);
    }

    public GlobalBossIgnorath save(GlobalBossIgnorath boss) {
        return repo.save(boss);
    }
    
    public GlobalBossIgnorath attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }

   

}
