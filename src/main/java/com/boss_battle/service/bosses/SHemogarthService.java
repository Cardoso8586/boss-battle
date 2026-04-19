package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossSHemogarth;
import com.boss_battle.repository.SHemogarthRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SHemogarthService {

    private static final long MAX_ATTACK = 700;
    private static final long MAX_INTERVAL = 1000;
    private static final long MAX_REWARD_BOSS = 400_000;
    private static final long MAX_EXP = 53000;
    private static final long MAX_HP = 600_000;

    @Autowired
    private SHemogarthRepository repo;

    public GlobalBossSHemogarth get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossSHemogarth createDefaultBoss() {

        GlobalBossSHemogarth boss = new GlobalBossSHemogarth();

        boss.setName("S-HEMOGARTH");
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_s_hemogarth.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossSHemogarth save(GlobalBossSHemogarth boss) {
        return repo.save(boss);
    }

    public GlobalBossSHemogarth attack(long damage) {

        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }

    //===========================================================
    public void aplicarEscalamentoSHemogarth(GlobalBossSHemogarth boss) {

        Random random = new Random();

        long min = 10;
        long max = 100;

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

            boss.setRewardBoss(valorsetRewardBoss + 2);

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

            boss.setAttackPower(valorAtaque + 2);

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