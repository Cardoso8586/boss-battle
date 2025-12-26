package com.boss_battle.service;



import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossNoxar;
import com.boss_battle.repository.NoxarRepository;

@Service
public class NoxarService {

    @Autowired
    private NoxarRepository repo;

    public GlobalBossNoxar get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossNoxar createDefaultBoss() {
    	GlobalBossNoxar boss = new GlobalBossNoxar();
         boss.setName("UMBRA XIS");
         boss.setMaxHp(50_000L);
         boss.setCurrentHp(50_000L);
         boss.setAlive(true);
         boss.setImageUrl("images/boss_umbraxis.webp");
         boss.setSpawnedAt(LocalDateTime.now());
         boss.setRespawnCooldownSeconds(5400L);
         boss.setSpawnCount(1);
         boss.setRewardBoss(50_000L);
         boss.setRewardExp(2200L);
        return repo.save(boss);
    }

    public GlobalBossNoxar save(GlobalBossNoxar boss) {
        return repo.save(boss);
    }
    
    public GlobalBossNoxar attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }

   

}
