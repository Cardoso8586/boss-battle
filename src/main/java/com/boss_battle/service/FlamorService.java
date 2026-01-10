package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossFlamor;
import com.boss_battle.repository.FlamorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FlamorService {

    @Autowired
    private FlamorRepository repo;

    public GlobalBossFlamor get() {
    	  return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossFlamor createDefaultBoss() {
    	GlobalBossFlamor boss = new GlobalBossFlamor();
         boss.setName("FLAMOR");
         boss.setMaxHp(250_000L);
         boss.setCurrentHp(250_000L);
         boss.setProcessingDeath(false);
         boss.setAlive(true);
         boss.setImageUrl("images/boss_flamor.webp");
         boss.setSpawnedAt(LocalDateTime.now());
         boss.setRespawnCooldownSeconds(3600L);
         boss.setSpawnCount(1);
         boss.setRewardBoss(80_000L);
         boss.setRewardExp(2500L);
        return repo.save(boss);
    }

    public GlobalBossFlamor save(GlobalBossFlamor boss) {
        return repo.save(boss);
    }
    
    public GlobalBossFlamor attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }

   

}
