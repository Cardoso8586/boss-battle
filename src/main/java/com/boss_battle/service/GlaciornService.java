package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossGlaciorn;
import com.boss_battle.repository.GlaciornRepository;

@Service
public class GlaciornService {

    @Autowired
    private GlaciornRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossGlaciorn get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista na base
     */
    public GlobalBossGlaciorn createDefaultBoss() {
        GlobalBossGlaciorn boss = new GlobalBossGlaciorn();

        boss.setName("GLACIORN");
        boss.setMaxHp(120_000L);
        boss.setCurrentHp(120_000L);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_glaciorn.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(4000L); // ~1h06
        boss.setSpawnCount(1);
        boss.setRewardBoss(65_000L);
        boss.setRewardExp(1700L);

        return repo.save(boss);
    }

    /**
     * Apenas salva alterações
     */
    public GlobalBossGlaciorn save(GlobalBossGlaciorn boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss e salva os dados atualizados
     */
    public GlobalBossGlaciorn attack(long damage) {
        GlobalBossGlaciorn boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss Glaciorn não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}

