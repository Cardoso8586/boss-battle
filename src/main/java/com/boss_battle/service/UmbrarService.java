package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossUmbrar;
import com.boss_battle.repository.UmbrarRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UmbrarService {

    @Autowired
    private UmbrarRepository repo;

    public GlobalBossUmbrar get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossUmbrar createDefaultBoss() {
    	GlobalBossUmbrar boss = new GlobalBossUmbrar();
         boss.setName("UMBRAR");
         boss.setMaxHp(42_000L);
         boss.setCurrentHp(42_000L);
         boss.setAlive(true);
         boss.setImageUrl("images/boss_umbrar.webp");
         boss.setSpawnedAt(LocalDateTime.now());
         boss.setRespawnCooldownSeconds(4500);
         boss.setSpawnCount(1);
         boss.setRewardBoss(40_000L);
         boss.setRewardExp(1800);
        return repo.save(boss);
    }

    public GlobalBossUmbrar save(GlobalBossUmbrar boss) {
        return repo.save(boss);
    }
    
    public GlobalBossUmbrar attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }

   

}
