package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossAsterion;
import com.boss_battle.repository.AsterionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AsterionService {

    private static final long MAX_ATTACK = 2_000L;
    private static final long MAX_INTERVAL = 700L;
    private static final long MAX_REWARD_BOSS = 500_000L;
    private static final long MAX_EXP = 100_000L;
    private static final long MAX_HP = 500_000L;

    @Autowired
    private AsterionRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossAsterion get() {

        return repo.findById(1L)
                .orElseGet(() -> createDefaultBoss());
    }

    // Cria boss padrão
    public GlobalBossAsterion createDefaultBoss() {

        GlobalBossAsterion boss = new GlobalBossAsterion();

        boss.setName("ASTERION");

        aplicarEscalamentoAsterion(boss);

        boss.setProcessingDeath(false);
        boss.setRewardDistributed(false);

        boss.setAlive(true);

        boss.setImageUrl("images/boss_asterion.webp");

        boss.setSpawnedAt(LocalDateTime.now());

        // 14 horas
        boss.setRespawnCooldownSeconds(50_400L);

        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossAsterion save(GlobalBossAsterion boss) {
        return repo.save(boss);
    }

    // ==========================================================
    // Evolução do boss
    // ==========================================================
    public void aplicarEscalamentoAsterion(GlobalBossAsterion boss) {

        Random random = new Random();

        long min = 200L;
        long max = 900L;

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

            boss.setRewardBoss(valorRewardBoss + 15L);

        } else {

            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        // ============================
        // XP
        // ============================

        if (valorXp < MAX_EXP) {

            boss.setRewardExp(valorXp + 10L);

        } else {

            boss.setRewardExp(MAX_EXP);
        }

        // ============================
        // Ataque
        // ============================

        if (valorAtaque < MAX_ATTACK) {

            boss.setAttackPower(valorAtaque + 10L);

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
    public GlobalBossAsterion attack(long damage) {

        GlobalBossAsterion boss = get();

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}