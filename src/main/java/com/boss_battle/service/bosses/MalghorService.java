package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossMalghor;
import com.boss_battle.repository.MalghorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MalghorService {

    private static final long MAX_ATTACK = 2_000L;
    private static final long MAX_INTERVAL = 600L;
    private static final long MAX_REWARD_BOSS = 400_000L;
    private static final long MAX_EXP = 100_000L;
    private static final long MAX_HP = 500_000L;

    @Autowired
    private MalghorRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossMalghor get() {

        return repo.findById(1L)
                .orElseGet(() -> createDefaultBoss());
    }

    // Cria boss padrão
    public GlobalBossMalghor createDefaultBoss() {

        GlobalBossMalghor boss = new GlobalBossMalghor();

        boss.setName("MALGHOR");

        aplicarEscalamentoMalghor(boss);

        boss.setProcessingDeath(false);
        boss.setRewardDistributed(false);

        boss.setAlive(true);

        boss.setImageUrl("images/boss_malghor.webp");

        boss.setSpawnedAt(LocalDateTime.now());

        // 10 horas
        boss.setRespawnCooldownSeconds(36_000L);

        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossMalghor save(GlobalBossMalghor boss) {
        return repo.save(boss);
    }

    // ==========================================================
    // Evolução do boss
    // ==========================================================
    public void aplicarEscalamentoMalghor(GlobalBossMalghor boss) {

        Random random = new Random();

        long min = 180L;
        long max = 550L;

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

            boss.setRewardBoss(valorRewardBoss + 3L);

        } else {

            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        // ============================
        // XP
        // ============================

        if (valorXp < MAX_EXP) {

            boss.setRewardExp(valorXp + 5L);

        } else {

            boss.setRewardExp(MAX_EXP);
        }

        // ============================
        // Ataque
        // ============================

        if (valorAtaque < MAX_ATTACK) {

            boss.setAttackPower(valorAtaque + 4L);

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
    public GlobalBossMalghor attack(long damage) {

        GlobalBossMalghor boss = get();

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}