package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossNoctharion;
import com.boss_battle.repository.NoctharionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NoctharionService {

    @Autowired
    private NoctharionRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossNoctharion get() {
        return repo.findById(1L).orElseGet(this::createDefaultBoss);
    }

    // Cria o boss padrão
    public GlobalBossNoctharion createDefaultBoss() {
    	GlobalBossNoctharion boss = new GlobalBossNoctharion();

        boss.setName("NOCTHARION");
        boss.setMaxHp(180_000L);
        boss.setCurrentHp(180_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);

        boss.setAttackPower(420L);
        boss.setAttackIntervalSeconds(30L);

        boss.setImageUrl("images/boss_noctharion.webp");

        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(5400); // 80 minutos
        boss.setSpawnCount(1);

        boss.setRewardBoss(120_000L);
        boss.setRewardExp(6200L);

        return repo.save(boss);
    }

    public GlobalBossNoctharion save(GlobalBossNoctharion boss) {
        return repo.save(boss);
    }

    // Ataque direto (usado em auto-attack ou debug)
    public GlobalBossNoctharion attack(long damage) {
    	GlobalBossNoctharion boss = get();
        boss.applyDamage(damage);
        return repo.save(boss);
    }
}
