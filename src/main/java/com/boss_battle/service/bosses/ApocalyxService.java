package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossApocalyx;
import com.boss_battle.repository.ApocalyxRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ApocalyxService {

    private static final long MAX_ATTACK = 5000;
    private static final long MAX_INTERVAL = 1000;
    private static final long MAX_REWARD_BOSS = 500_000;
    private static final long MAX_EXP = 300_000;
    private static final long MAX_HP = 5_000_000;

    @Autowired
    private ApocalyxRepository repo;

    public GlobalBossApocalyx get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossApocalyx createDefaultBoss() {

        GlobalBossApocalyx boss = new GlobalBossApocalyx();

        boss.setName("APOCALYX");
        boss.setProcessingDeath(false);
        boss.setRewardDistributed(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_apocalyx.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossApocalyx save(GlobalBossApocalyx boss) {
        return repo.save(boss);
    }

    public GlobalBossApocalyx attack(long damage) {

        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }

    public void aplicarEscalamentoApocalyx(GlobalBossApocalyx boss) {

        Random random = new Random();

        long min = 300;
        long max = 800;

        long incrementarUp = random.nextLong(min, max + 1);

        long valorHpMax = boss.getMaxHp();
        long valorCur = boss.getCurrentHp();
        long valorAtaque = boss.getAttackPower();
        long valorIntervalSeconds = boss.getAttackIntervalSeconds();
        long valorsetRewardBoss = boss.getRewardBoss();

        if (valorHpMax < MAX_HP) {
            boss.setMaxHp(valorHpMax + incrementarUp);
            boss.setCurrentHp(valorCur + incrementarUp);
        } else {
            boss.setMaxHp(MAX_HP);
            boss.setCurrentHp(MAX_HP);
        }

        if (valorsetRewardBoss < MAX_REWARD_BOSS) {
            boss.setRewardBoss(valorsetRewardBoss + 1);
        } else {
            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        long valorXp = boss.getRewardExp();

        if (valorXp < MAX_EXP) {
            boss.setRewardExp(valorXp + 1);
        } else {
            boss.setRewardExp(MAX_EXP);
        }

        if (valorAtaque < MAX_ATTACK) {
            boss.setAttackPower(valorAtaque + 1);
        } else {
            boss.setAttackPower(MAX_ATTACK);
        }

        if (valorIntervalSeconds < MAX_INTERVAL) {
            boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);
        } else {
            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }
    }
}