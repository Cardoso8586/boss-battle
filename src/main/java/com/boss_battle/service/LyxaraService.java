package com.boss_battle.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossLyxara;
import com.boss_battle.repository.LyxaraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LyxaraService {

    @Autowired
    private LyxaraRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossLyxara get() {
        return repo.findById(1L).orElseGet(this::createDefaultBoss);
    }

    // Cria o boss padrão
    public GlobalBossLyxara createDefaultBoss() {
        GlobalBossLyxara boss = new GlobalBossLyxara();

        boss.setName("LYXARA — A Soberana Sombria");
        boss.setMaxHp(40_000L);
        boss.setCurrentHp(40_000L);
        boss.setAlive(true);

        boss.setAttackPower(140L);
        boss.setAttackIntervalSeconds(42L);

        boss.setImageUrl("images/boss_lyxara.webp");

        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(4800L); // 80 minutos
        boss.setSpawnCount(1);

        boss.setRewardBoss(40_000L);
        boss.setRewardExp(3000L);

        return repo.save(boss);
    }

    public GlobalBossLyxara save(GlobalBossLyxara boss) {
        return repo.save(boss);
    }

    // Ataque direto (usado em auto-attack ou debug)
    public GlobalBossLyxara attack(long damage) {
        GlobalBossLyxara boss = get();
        boss.applyDamage(damage);
        return repo.save(boss);
    }
}
