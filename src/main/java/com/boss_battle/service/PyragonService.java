package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossPyragon;
import com.boss_battle.repository.PyragonRepository;

@Service
public class PyragonService {

    @Autowired
    private PyragonRepository repo;

    /**
     * Retorna o boss com ID fixo 1
     */
    public GlobalBossPyragon get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    /**
     * Cria o boss caso não exista na base
     */
    public GlobalBossPyragon createDefaultBoss() {
        GlobalBossPyragon boss = new GlobalBossPyragon();

        boss.setName("PYRAGON");
        boss.setMaxHp(150_000L);
        boss.setCurrentHp(150_000L);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_pyragon.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(3600L); // 1 hora
        boss.setSpawnCount(1);
        boss.setRewardBoss(75_000L);
        boss.setRewardExp(1800L);

        return repo.save(boss);
    }

    /**
     * Apenas salva alterações
     */
    public GlobalBossPyragon save(GlobalBossPyragon boss) {
        return repo.save(boss);
    }

    /**
     * Ataca o boss e salva os dados atualizados
     */
    public GlobalBossPyragon attack(long damage) {
        GlobalBossPyragon boss = repo.findById(1L)
            .orElseThrow(() -> new RuntimeException("Boss Pyragon não encontrado"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}
