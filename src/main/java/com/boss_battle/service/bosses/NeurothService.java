package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossNeuroth;
import com.boss_battle.repository.NeurothRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NeurothService {

    private static final long MAX_ATTACK = 1_500L;
    private static final long MAX_INTERVAL = 500L;
    private static final long MAX_REWARD_BOSS = 300_000L;
    private static final long MAX_EXP = 80_000L;
    private static final long MAX_HP = 500_000L;

    @Autowired
    private NeurothRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossNeuroth get() {

        return repo.findById(1L)
                .orElseGet(() -> createDefaultBoss());
    }

    // Cria boss padrão
    public GlobalBossNeuroth createDefaultBoss() {

        GlobalBossNeuroth boss = new GlobalBossNeuroth();

        boss.setName("NEUROTH");

        aplicarEscalamentoNeuroth(boss);

        boss.setProcessingDeath(false);
        boss.setRewardDistributed(false);

        boss.setAlive(true);

        boss.setImageUrl("images/boss_neuroth.webp");

        boss.setSpawnedAt(LocalDateTime.now());

        // 8 horas
        boss.setRespawnCooldownSeconds(28_800L);

        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossNeuroth save(GlobalBossNeuroth boss) {
        return repo.save(boss);
    }

    // ==========================================================
    // Evolução do boss
    // ==========================================================
    public void aplicarEscalamentoNeuroth(GlobalBossNeuroth boss) {

        Random random = new Random();

        long min = 50L;
        long max = 250L;

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

            boss.setRewardBoss(valorRewardBoss + 2L);

        } else {

            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        // ============================
        // XP
        // ============================

        if (valorXp < MAX_EXP) {

            boss.setRewardExp(valorXp + 3L);

        } else {

            boss.setRewardExp(MAX_EXP);
        }

        // ============================
        // Ataque
        // ============================

        if (valorAtaque < MAX_ATTACK) {

            boss.setAttackPower(valorAtaque + 3L);

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
    public GlobalBossNeuroth attack(long damage) {

        GlobalBossNeuroth boss = get();

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}