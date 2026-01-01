

package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.GlobalBossUmbraxis;
import com.boss_battle.repository.UmbraxisRepository;

@Service
@Transactional
public class UmbraxisService {

    @Autowired
    private UmbraxisRepository repo;

    public GlobalBossUmbraxis get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossUmbraxis createDefaultBoss() {
    	GlobalBossUmbraxis boss = new GlobalBossUmbraxis();
         boss.setName("UMBRA XIS");
         boss.setMaxHp(200_000);
         boss.setCurrentHp(200_000);
         boss.setAlive(true);
         boss.setImageUrl("images/boss_umbraxis.webp");
         boss.setSpawnedAt(LocalDateTime.now());
         boss.setRespawnCooldownSeconds(7200);
         boss.setSpawnCount(1);
         boss.setRewardBoss(100_000);
         boss.setRewardExp(2500);
        return repo.save(boss);
    }

    public GlobalBossUmbraxis save(GlobalBossUmbraxis boss) {
        return repo.save(boss);
    }
    
    public GlobalBossUmbraxis attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }

   

}
