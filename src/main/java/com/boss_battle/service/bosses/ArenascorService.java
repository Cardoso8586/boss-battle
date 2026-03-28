package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossArenascor;
import com.boss_battle.repository.ArenascorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ArenascorService {

    private static final long MAX_ATTACK = 1200;
    private static final long MAX_INTERVAL = 1100;
    private static final long MAX_REWARD_BOSS = 370_000;
    private static final long MAX_EXP = 52_000;
    private static final long MAX_HP = 800_000;

    @Autowired
    private ArenascorRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossArenascor get() {
        return repo.findById(1L).orElseGet(this::createDefaultBoss);
    }

    // Cria o boss padrão
    public GlobalBossArenascor createDefaultBoss() {
        GlobalBossArenascor boss = new GlobalBossArenascor();

        boss.setName("ARENASCOR, GUARDIÃO DO DESERTO ARDENTE");
        boss.setProcessingDeath(false);
        boss.setAlive(true);

        boss.setImageUrl("images/boss_arenascor.webp");

        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(14_400L); // 4 horas
        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossArenascor save(GlobalBossArenascor boss) {
        return repo.save(boss);
    }

    // Ataque direto (usado em auto‑attack ou debug)
    public GlobalBossArenascor attack(long damage) {
        GlobalBossArenascor boss = get();
        boss.applyDamage(damage);
        return repo.save(boss);
    }

    //===========================================================
    // incrementar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoArenascor(GlobalBossArenascor boss) {

        Random random = new Random();
        long min = 10;
        long max = 100;
        long incrementarUp = random.nextLong(min, max + 1);

        long valorHpMax = boss.getMaxHp();
        long valorCur = boss.getCurrentHp();
        long valorAtaque = boss.getAttackPower();
        long valorIntervalSeconds = boss.getAttackIntervalSeconds();
        long valorsetRewardBoss = boss.getRewardBoss();

        // Limitar HP
        if (valorHpMax < MAX_HP) {
            boss.setMaxHp(valorHpMax + incrementarUp);
            boss.setCurrentHp(valorCur + incrementarUp);
        } else {
            boss.setMaxHp(MAX_HP);
            boss.setCurrentHp(MAX_HP);
        }

        // Limitar recompensa
        if (valorsetRewardBoss < MAX_REWARD_BOSS) {
            boss.setRewardBoss(valorsetRewardBoss + 1);
        } else {
            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        // Limitar XP
        long valorXp = boss.getRewardExp();
        if (valorXp < MAX_EXP) {
            boss.setRewardExp(valorXp + 1);
        } else {
            boss.setRewardExp(MAX_EXP);
        }

        // Limitar Evolução do ataque
        if (valorAtaque < MAX_ATTACK) {
            boss.setAttackPower(valorAtaque + 2);
        } else {
            boss.setAttackPower(MAX_ATTACK);
        }

        // Limitar Evolução do intervalo
        if (valorIntervalSeconds < MAX_INTERVAL) {
            boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);
        } else {
            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }
    }
}