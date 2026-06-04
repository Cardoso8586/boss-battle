package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossHyperionX;
import com.boss_battle.repository.HyperionXRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HyperionXService {

    private static final long MAX_ATTACK = 2_800;
    private static final long MAX_INTERVAL = 1_800;
    private static final long MAX_REWARD_BOSS = 500_000;
    private static final long MAX_EXP = 100_000;
    private static final long MAX_HP = 700_000;

    @Autowired
    private HyperionXRepository repo;

    public GlobalBossHyperionX get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossHyperionX createDefaultBoss() {
        GlobalBossHyperionX boss = new GlobalBossHyperionX();
        boss.setName("HYPERION-X");
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_hyperion_x.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(3600);
        boss.setSpawnCount(1);
        return repo.save(boss);
    }

    public GlobalBossHyperionX save(GlobalBossHyperionX boss) {
        return repo.save(boss);
    }

    public GlobalBossHyperionX attack(long damage) {

        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }

    // ===========================================================
    // incrementar hp, toda vez que o boss for derrotado
    // ===========================================================
    public void aplicarEscalamentoHyperionX(GlobalBossHyperionX boss) {

        Random random = new Random();

        long min = 100;
        long max = 500;
        long incrementarUp = random.nextLong(min, max + 1);

        long valorHpMax = boss.getMaxHp();
        long valorCur = boss.getCurrentHp();
        long valorAtaque = boss.getAttackPower();
        long valorIntervalSeconds = boss.getAttackIntervalSeconds();
        long valorsetRewardBoss = boss.getRewardBoss();

        // Limitar hp
        if (valorHpMax < MAX_HP) {

            boss.setMaxHp(valorHpMax + incrementarUp);
            boss.setCurrentHp(valorCur + incrementarUp);

        } else {

            boss.setMaxHp(MAX_HP);
            boss.setCurrentHp(MAX_HP);
        }

        // Limitar recompensa
        if (valorsetRewardBoss < MAX_REWARD_BOSS) {

            boss.setRewardBoss(valorsetRewardBoss + 11);

        } else {

            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        // Limitar XP
        long valorXp = boss.getRewardExp();

        if (valorXp < MAX_EXP) {

            boss.setRewardExp(valorXp + 11);

        } else {

            boss.setRewardExp(MAX_EXP);
        }

        // Limitar evolução do ataque
        if (valorAtaque < MAX_ATTACK) {

            boss.setAttackPower(valorAtaque + 25);

        } else {

            boss.setAttackPower(MAX_ATTACK);
        }

        // Limitar evolução do intervalo
        if (valorIntervalSeconds < MAX_INTERVAL) {

            boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);

        } else {

            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }
    }
}