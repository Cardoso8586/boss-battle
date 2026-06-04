package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossZerathon;
import com.boss_battle.repository.ZerathonRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ZerathonService {

    private static final long MAX_ATTACK = 1_800;
    private static final long MAX_INTERVAL = 1500;
    private static final long MAX_REWARD_BOSS = 300_000;
    private static final long MAX_EXP = 100_000;
    private static final long MAX_HP = 700_000;

    @Autowired
    private ZerathonRepository repo;

    public GlobalBossZerathon get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossZerathon createDefaultBoss() {
        GlobalBossZerathon boss = new GlobalBossZerathon();
        boss.setName("ZERATHON");
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_zerathon.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(3600);
        boss.setSpawnCount(1);
        return repo.save(boss);
    }

    public GlobalBossZerathon save(GlobalBossZerathon boss) {
        return repo.save(boss);
    }

    public GlobalBossZerathon attack(long damage) {
        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }

    public void aplicarEscalamentoZerathon(GlobalBossZerathon boss) {

        Random random = new Random();
        long min = 100;
        long max = 300;
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
            boss.setRewardBoss(valorsetRewardBoss + 5);
        } else {
            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        long valorXp = boss.getRewardExp();
        if (valorXp < MAX_EXP) {
            boss.setRewardExp(valorXp + 5);
        } else {
            boss.setRewardExp(MAX_EXP);
        }

        if (valorAtaque < MAX_ATTACK) {
            boss.setAttackPower(valorAtaque + 15);
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