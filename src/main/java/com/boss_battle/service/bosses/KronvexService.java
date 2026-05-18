package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossKronvex;
import com.boss_battle.repository.KronvexRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class KronvexService {

    private static final long MAX_ATTACK = 2_000L;
    private static final long MAX_INTERVAL = 800L;
    private static final long MAX_REWARD_BOSS = 500_000L;
    private static final long MAX_EXP = 150_000L;
    private static final long MAX_HP = 800_000L;

    @Autowired
    private KronvexRepository repo;

    public GlobalBossKronvex get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossKronvex createDefaultBoss() {
        GlobalBossKronvex boss = new GlobalBossKronvex();

        boss.setName("KRONVEX");
        aplicarEscalamentoKronvex(boss);

        boss.setProcessingDeath(false);
        boss.setRewardDistributed(false);
        boss.setAlive(true);

        boss.setImageUrl("images/boss_kronvex.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(64_800L);
        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossKronvex save(GlobalBossKronvex boss) {
        return repo.save(boss);
    }

    public void aplicarEscalamentoKronvex(GlobalBossKronvex boss) {
        Random random = new Random();

        long min = 300L;
        long max = 800L;
        long incrementarUp = random.nextLong(min, max + 1);

        long valorHpMax = boss.getMaxHp();
        long valorCur = boss.getCurrentHp();
        long valorAtaque = boss.getAttackPower();
        long valorIntervalSeconds = boss.getAttackIntervalSeconds();
        long valorRewardBoss = boss.getRewardBoss();
        long valorXp = boss.getRewardExp();

        if (valorHpMax < MAX_HP) {
            boss.setMaxHp(valorHpMax + incrementarUp);
            boss.setCurrentHp(valorCur + incrementarUp);
        } else {
            boss.setMaxHp(MAX_HP);
            boss.setCurrentHp(MAX_HP);
        }

        if (valorRewardBoss < MAX_REWARD_BOSS) {
            boss.setRewardBoss(valorRewardBoss + 10L);
        } else {
            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        if (valorXp < MAX_EXP) {
            boss.setRewardExp(valorXp + 25L);
        } else {
            boss.setRewardExp(MAX_EXP);
        }

        if (valorAtaque < MAX_ATTACK) {
            boss.setAttackPower(valorAtaque + 20L);
        } else {
            boss.setAttackPower(MAX_ATTACK);
        }

        if (valorIntervalSeconds < MAX_INTERVAL) {
            boss.setAttackIntervalSeconds(valorIntervalSeconds + 1L);
        } else {
            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }
    }

    public GlobalBossKronvex attack(long damage) {
        GlobalBossKronvex boss = get();
        boss.applyDamage(damage);
        return repo.save(boss);
    }
}