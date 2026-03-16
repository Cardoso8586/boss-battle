package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossZargoth;
import com.boss_battle.repository.ZargothRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ZargothService {

    private static final long MAX_ATTACK = 500;
    private static final long MAX_INTERVAL = 800;
    private static final long MAX_REWARD_BOSS = 700_000;
    private static final long MAX_EXP = 53000;
    private static final long MAX_HP = 700_000;

    @Autowired
    private ZargothRepository repo;

    public GlobalBossZargoth get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossZargoth createDefaultBoss() {

        GlobalBossZargoth boss = new GlobalBossZargoth();

        boss.setName("ZARGOTH");
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_zargoth.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossZargoth save(GlobalBossZargoth boss) {
        return repo.save(boss);
    }

    public GlobalBossZargoth attack(long damage) {

        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }

    //===========================================================
    // incrementar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoZargoth(GlobalBossZargoth boss) {

        Random random = new Random();

        long min = 10;
        long max = 100;

        long incrementarUp = random.nextLong(min, max + 1);

        long valorHpMax = boss.getMaxHp();
        long valorCur = boss.getCurrentHp();
        long valorAtaque = boss.getAttackPower();
        long valorIntervalSeconds = boss.getAttackIntervalSeconds();
        long valorsetRewardBoss = boss.getRewardBoss();

        // Limitar hp
        if (valorHpMax < MAX_HP) {

            boss.setMaxHp(valorHpMax + incrementarUp);
            boss.setCurrentHp(valorCur + incrementarUp);

        } else {

            boss.setMaxHp(MAX_HP);
            boss.setCurrentHp(MAX_HP);
        }

        // Limitar recompensa
        if (valorsetRewardBoss < MAX_REWARD_BOSS) {

            boss.setRewardBoss(valorsetRewardBoss + 1);

        } else {

            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        // Limitar xp
        long valorXp = boss.getRewardExp();

        if (valorXp < MAX_EXP) {

            boss.setRewardExp(valorXp + 1);

        } else {

            boss.setRewardExp(MAX_EXP);
        }

        // Limitar Evolução do ataque
        if (valorAtaque < MAX_ATTACK) {

            boss.setAttackPower(valorAtaque + 2);

        } else {

            boss.setAttackPower(MAX_ATTACK);
        }

        // Limitar Evolução do intervalo
        if (valorIntervalSeconds < MAX_INTERVAL) {

            boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);

        } else {

            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }
    }
}