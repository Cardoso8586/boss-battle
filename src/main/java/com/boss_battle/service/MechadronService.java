package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossMechadron;
import com.boss_battle.repository.MechadronRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MechadronService {

    @Autowired
    private MechadronRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossMechadron get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista
     */
    public GlobalBossMechadron createDefaultBoss() {
        GlobalBossMechadron boss = new GlobalBossMechadron();

        boss.setName("MECHADRON");
        boss.setMaxHp(220_000L);
        boss.setCurrentHp(220_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_mechadron.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(5400L);
        boss.setSpawnCount(1);
        boss.setRewardBoss(110_000L);
        boss.setRewardExp(2800L);

        return repo.save(boss);
    }

    /**
     * Salva alterações
     */
    public GlobalBossMechadron save(GlobalBossMechadron boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss
     */
    public GlobalBossMechadron attack(long damage) {
        GlobalBossMechadron boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss MECHADRON não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}
