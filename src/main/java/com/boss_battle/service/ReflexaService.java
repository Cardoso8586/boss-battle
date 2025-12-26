package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossReflexa;
import com.boss_battle.repository.ReflexaRepository;

@Service
public class ReflexaService {

    @Autowired
    private ReflexaRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossReflexa get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista na base
     */
    public GlobalBossReflexa createDefaultBoss() {
        GlobalBossReflexa boss = new GlobalBossReflexa();

        boss.setName("REFLEXA");
        boss.setMaxHp(170_000L);
        boss.setCurrentHp(170_000L);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_reflexa.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(4800L);
        boss.setSpawnCount(1);
        boss.setRewardBoss(90_000L);
        boss.setRewardExp(2400L);

        return repo.save(boss);
    }

    /**
     * Apenas salva alterações
     */
    public GlobalBossReflexa save(GlobalBossReflexa boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss e salva os dados atualizados
     */
    public GlobalBossReflexa attack(long damage) {
        GlobalBossReflexa boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss REFLEXA não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}
