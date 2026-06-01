package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossNovarok;
import com.boss_battle.repository.NovarokRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NovarokService {

    private static final long MAX_ATTACK = 1_500L;
    private static final long MAX_INTERVAL = 700L;
    private static final long MAX_REWARD_BOSS = 200_000L;
    private static final long MAX_EXP = 100_000L;
    private static final long MAX_HP = 500_000L;

    @Autowired
    private NovarokRepository repo;

    public GlobalBossNovarok get() {
        return repo.findById(1L)
                .orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossNovarok createDefaultBoss() {

        GlobalBossNovarok boss = new GlobalBossNovarok();

        boss.setName("NOVAROK");

        aplicarEscalamentoNovarok(boss);

        boss.setProcessingDeath(false);
        boss.setRewardDistributed(false);
        boss.setAlive(true);

        boss.setImageUrl("images/boss_novarok.webp");

        boss.setSpawnedAt(LocalDateTime.now());

        // 12 horas
        boss.setRespawnCooldownSeconds(43_200L);

        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossNovarok save(GlobalBossNovarok boss) {
        return repo.save(boss);
    }

    public void aplicarEscalamentoNovarok(GlobalBossNovarok boss) {

        Random random = new Random();

        long min = 100L;
        long max = 500L;

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
            boss.setRewardBoss(valorRewardBoss + 13L);
        } else {
            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        if (valorXp < MAX_EXP) {
            boss.setRewardExp(valorXp + 17L);
        } else {
            boss.setRewardExp(MAX_EXP);
        }

        if (valorAtaque < MAX_ATTACK) {
            boss.setAttackPower(valorAtaque + 35L);
        } else {
            boss.setAttackPower(MAX_ATTACK);
        }

        if (valorIntervalSeconds < MAX_INTERVAL) {
            boss.setAttackIntervalSeconds(valorIntervalSeconds + 1L);
        } else {
            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }
    }

    public GlobalBossNovarok attack(long damage) {

        GlobalBossNovarok boss = get();

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}