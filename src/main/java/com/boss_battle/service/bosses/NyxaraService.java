package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossNyxara;
import com.boss_battle.repository.NyxaraRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NyxaraService {

    private static final long MAX_ATTACK = 4_000L;
    private static final long MAX_INTERVAL = 1_000L;
    private static final long MAX_REWARD_BOSS = 500_000L;
    private static final long MAX_EXP = 100_000L;
    private static final long MAX_HP = 800_000L;

    @Autowired
    private NyxaraRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossNyxara get() {

        return repo.findById(1L)
                .orElseGet(() -> createDefaultBoss());
    }

    // Cria boss padrão
    public GlobalBossNyxara createDefaultBoss() {

        GlobalBossNyxara boss = new GlobalBossNyxara();

        boss.setName("NYXARA");

        aplicarEscalamentoNyxara(boss);

        boss.setProcessingDeath(false);
        boss.setRewardDistributed(false);

        boss.setAlive(true);

        boss.setImageUrl("images/boss_nyxara.webp");

        boss.setSpawnedAt(LocalDateTime.now());

        // 22 horas
        boss.setRespawnCooldownSeconds(79_200L);

        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossNyxara save(GlobalBossNyxara boss) {
        return repo.save(boss);
    }

    // ==========================================================
    // Evolução da boss
    // ==========================================================
    public void aplicarEscalamentoNyxara(GlobalBossNyxara boss) {

        Random random = new Random();

        long min = 400L;
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

            boss.setRewardBoss(valorRewardBoss + 20L);

        } else {

            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        // ============================
        // XP
        // ============================

        if (valorXp < MAX_EXP) {

            boss.setRewardExp(valorXp + 25L);

        } else {

            boss.setRewardExp(MAX_EXP);
        }

        // ============================
        // Ataque
        // ============================

        if (valorAtaque < MAX_ATTACK) {

            boss.setAttackPower(valorAtaque + 20L);

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
    public GlobalBossNyxara attack(long damage) {

        GlobalBossNyxara boss = get();

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}