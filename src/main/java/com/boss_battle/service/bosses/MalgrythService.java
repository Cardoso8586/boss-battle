package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossMalgryth;
import com.boss_battle.repository.MalgrythRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MalgrythService {

    private static final long MAX_ATTACK = 700;
    private static final long MAX_INTERVAL = 1000;
    private static final long MAX_REWARD_BOSS = 700_000;
    private static final long MAX_EXP = 53000;
    private static final long MAX_HP = 700_000;

    @Autowired
    private MalgrythRepository repo;

    public GlobalBossMalgryth get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossMalgryth createDefaultBoss() {

        GlobalBossMalgryth boss = new GlobalBossMalgryth();

        boss.setName("MALGRYTH");
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_malgryth.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossMalgryth save(GlobalBossMalgryth boss) {
        return repo.save(boss);
    }

    public GlobalBossMalgryth attack(long damage) {

        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }

    //===========================================================
    // incrementar hp toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoMalgryth(GlobalBossMalgryth boss) {

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