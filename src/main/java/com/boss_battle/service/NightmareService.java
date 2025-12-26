package com.boss_battle.service;


import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossNightmare;
import com.boss_battle.repository.NightmareRepository;

@Service
public class NightmareService {

    @Autowired
    private NightmareRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossNightmare get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista na base
     */
    public GlobalBossNightmare createDefaultBoss() {
    	GlobalBossNightmare boss = new GlobalBossNightmare();

        boss.setName("NIGHTMARE");
        boss.setMaxHp(250_000);
        boss.setCurrentHp(250_000);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_nightmare.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(10800L); // 1 hora
        boss.setSpawnCount(1);
        boss.setRewardBoss(150_000);
        boss.setRewardExp(3000);

        return repo.save(boss);
    }

    /**
     * Apenas salva alterações
     */
    public GlobalBossNightmare save(GlobalBossNightmare boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss e salva os dados atualizados
     */
    public GlobalBossNightmare attack(long damage) {
    	GlobalBossNightmare boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss Azurion não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}
