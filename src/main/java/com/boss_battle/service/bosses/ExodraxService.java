package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossExodrax;
import com.boss_battle.repository.ExodraxRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ExodraxService {

    private static final long MAX_ATTACK = 3200;
    private static final long MAX_INTERVAL = 1000;
    private static final long MAX_REWARD_BOSS = 8000_000;
    private static final long MAX_EXP = 150_000;
    private static final long MAX_HP = 8_000_000;

    @Autowired
    private ExodraxRepository repo;

    public GlobalBossExodrax get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossExodrax createDefaultBoss() {

        GlobalBossExodrax boss = new GlobalBossExodrax();

        boss.setName("EXODRAX");
        boss.setProcessingDeath(false);
        boss.setRewardDistributed(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_exodrax.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossExodrax save(GlobalBossExodrax boss) {
        return repo.save(boss);
    }

    public GlobalBossExodrax attack(long damage) {

        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }

    //===========================================================
    // incrementar hp toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoExodrax(GlobalBossExodrax boss) {

        Random random = new Random();

        long min = 100;
        long max = 500;

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

        // Limitar recompensa boss
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

        // Evolução do ataque
        if (valorAtaque < MAX_ATTACK) {

            boss.setAttackPower(valorAtaque + 1);

        } else {

            boss.setAttackPower(MAX_ATTACK);
        }

        // Evolução intervalo de ataque
        if (valorIntervalSeconds < MAX_INTERVAL) {

            boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);

        } else {

            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }
    }
}