package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossOblivion;
import com.boss_battle.repository.OblivionRepository;

@Service
public class OblivionService {

    @Autowired
    private OblivionRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossOblivion get() {
        return repo.findById(1L).orElseGet(this::createDefaultBoss);
    }

    // Cria o boss padrão
    public GlobalBossOblivion createDefaultBoss() {
    	GlobalBossOblivion boss = new GlobalBossOblivion();

        boss.setName("OBLIVION");
        boss.setMaxHp(80_000L);
        boss.setCurrentHp(80_000L);
        boss.setAlive(true);

        boss.setAttackPower(200L);
        boss.setAttackIntervalSeconds(60L);

        boss.setImageUrl("images/boss_oblivion.webp");

        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(7200L); 
        boss.setSpawnCount(1);

        boss.setRewardBoss(75_000L);
        boss.setRewardExp(3000L);

        return repo.save(boss);
    }

    public GlobalBossOblivion save(GlobalBossOblivion boss) {
        return repo.save(boss);
    }

    // Ataque direto (usado em auto-attack ou debug)
    public GlobalBossOblivion attack(long damage) {
    	GlobalBossOblivion boss = get();
        boss.applyDamage(damage);
        return repo.save(boss);
    }
}