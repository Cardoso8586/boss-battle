package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossKryptar;
import com.boss_battle.repository.KryptarRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class KryptarService {

    private static final long MAX_ATTACK = 2_000L;
    private static final long MAX_INTERVAL = 900L;
    private static final long MAX_REWARD_BOSS = 400_000L;
    private static final long MAX_EXP = 100_000L;
    private static final long MAX_HP = 500_000L;

    @Autowired
    private KryptarRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossKryptar get() {

        return repo.findById(1L)
                .orElseGet(() -> createDefaultBoss());
    }

    // Cria boss padrão
    public GlobalBossKryptar createDefaultBoss() {

        GlobalBossKryptar boss = new GlobalBossKryptar();

        boss.setName("KRYPTAR");

        aplicarEscalamentoKryptar(boss);

        boss.setProcessingDeath(false);
        boss.setRewardDistributed(false);

        boss.setAlive(true);

        boss.setImageUrl("images/boss_kryptar.webp");

        boss.setSpawnedAt(LocalDateTime.now());

        // 20 horas
        boss.setRespawnCooldownSeconds(72_000L);

        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossKryptar save(GlobalBossKryptar boss) {
        return repo.save(boss);
    }

    // ==========================================================
    // Evolução do boss
    // ==========================================================
    public void aplicarEscalamentoKryptar(GlobalBossKryptar boss) {

        Random random = new Random();

        long min = 500L;
        long max = 1_000L;

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

            boss.setRewardBoss(valorRewardBoss + 10L);

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

            boss.setAttackPower(valorAtaque + 15L);

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
    public GlobalBossKryptar attack(long damage) {

        GlobalBossKryptar boss = get();

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}