package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossNexaron;
import com.boss_battle.repository.NexaronRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NexaronService {

    private static final long MAX_ATTACK = 1_800;
    private static final long MAX_INTERVAL = 1300;
    private static final long MAX_REWARD_BOSS = 400_000;
    private static final long MAX_EXP = 150_000;
    private static final long MAX_HP = 700_000;

    @Autowired
    private NexaronRepository repo;

    public GlobalBossNexaron get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossNexaron createDefaultBoss() {
        GlobalBossNexaron boss = new GlobalBossNexaron();
        boss.setName("NEXARON");
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_nexaron.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(3600);
        boss.setSpawnCount(1);
        return repo.save(boss);
    }

    public GlobalBossNexaron save(GlobalBossNexaron boss) {
        return repo.save(boss);
    }

    public GlobalBossNexaron attack(long damage) {
        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }

    public void aplicarEscalamentoNexaron(GlobalBossNexaron boss) {

        Random random = new Random();
        long min = 200;
        long max = 400;
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
            boss.setRewardBoss(valorsetRewardBoss + 11);
        } else {
            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        long valorXp = boss.getRewardExp();

        if (valorXp < MAX_EXP) {
            boss.setRewardExp(valorXp + 11);
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