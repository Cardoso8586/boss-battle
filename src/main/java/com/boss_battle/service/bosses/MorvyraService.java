package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossMorvyra;
import com.boss_battle.repository.MorvyraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MorvyraService {

    private static final long MAX_ATTACK = 1_500L;
    private static final long MAX_INTERVAL = 1_200L;
    private static final long MAX_REWARD_BOSS = 500_000L;
    private static final long MAX_EXP = 150_000L;
    private static final long MAX_HP = 700_000L;

    @Autowired
    private MorvyraRepository repo;

    public GlobalBossMorvyra get() {
        return repo.findById(1L)
                .orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossMorvyra createDefaultBoss() {

        GlobalBossMorvyra boss = new GlobalBossMorvyra();

        boss.setName("MORVYRA");

        aplicarEscalamentoMorvyra(boss);

        boss.setProcessingDeath(false);
        boss.setRewardDistributed(false);

        boss.setAlive(true);

        boss.setImageUrl("images/boss_morvyra.webp");

        boss.setSpawnedAt(LocalDateTime.now());

        // 24 horas
        boss.setRespawnCooldownSeconds(86_400L);

        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossMorvyra save(GlobalBossMorvyra boss) {
        return repo.save(boss);
    }

    // ==========================================================
    // Evolução da boss
    // ==========================================================
    public void aplicarEscalamentoMorvyra(GlobalBossMorvyra boss) {

        Random random = new Random();

        long min = 500L;
        long max = 1_500L;

        long incrementarUp =
                random.nextLong(min, max + 1);

        long valorHpMax = boss.getMaxHp();
        long valorCur = boss.getCurrentHp();
        long valorAtaque = boss.getAttackPower();

        long valorIntervalSeconds =
                boss.getAttackIntervalSeconds();

        long valorRewardBoss =
                boss.getRewardBoss();

        long valorXp =
                boss.getRewardExp();

        // ============================
        // HP
        // ============================

        if (valorHpMax < MAX_HP) {

            boss.setMaxHp(valorHpMax + incrementarUp);

            boss.setCurrentHp(valorCur + incrementarUp);

        } else {

            boss.setMaxHp(MAX_HP);
            boss.setCurrentHp(MAX_HP);
        }

        // ============================
        // Recompensa
        // ============================

        if (valorRewardBoss < MAX_REWARD_BOSS) {

            boss.setRewardBoss(valorRewardBoss + 50L);

        } else {

            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        // ============================
        // XP
        // ============================

        if (valorXp < MAX_EXP) {

            boss.setRewardExp(valorXp + 120L);

        } else {

            boss.setRewardExp(MAX_EXP);
        }

        // ============================
        // Ataque
        // ============================

        if (valorAtaque < MAX_ATTACK) {

            boss.setAttackPower(valorAtaque + 50L);

        } else {

            boss.setAttackPower(MAX_ATTACK);
        }

        // ============================
        // Intervalo ataque
        // ============================

        if (valorIntervalSeconds < MAX_INTERVAL) {

            boss.setAttackIntervalSeconds(
                    valorIntervalSeconds + 1L);

        } else {

            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }
    }

    // Ataque direto
    public GlobalBossMorvyra attack(long damage) {

        GlobalBossMorvyra boss = get();

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}