package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossPuppetrix;
import com.boss_battle.repository.PuppetrixRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PuppetrixService {

    private static final long MAX_ATTACK = 700;
    private static final long MAX_INTERVAL = 1100;
    private static final long MAX_REWARD_BOSS = 500_000;
    private static final long MAX_EXP = 53_000;
    private static final long MAX_HP = 700_000;

    @Autowired
    private PuppetrixRepository repo;

    public GlobalBossPuppetrix get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossPuppetrix createDefaultBoss() {

        GlobalBossPuppetrix boss = new GlobalBossPuppetrix();

        boss.setName("PUPPETRIX");
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_puppetrix.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossPuppetrix save(GlobalBossPuppetrix boss) {
        return repo.save(boss);
    }

    public GlobalBossPuppetrix attack(long damage) {

        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }

    //===========================================================
    // Escalamento quando o boss é derrotado
    //===========================================================
    public void aplicarEscalamentoPuppetrix(GlobalBossPuppetrix boss) {

        Random random = new Random();

        long min = 10;
        long max = 100;

        long incrementarUp = random.nextLong(min, max + 1);

        long valorHpMax = boss.getMaxHp();
        long valorCur = boss.getCurrentHp();
        long valorAtaque = boss.getAttackPower();
        long valorIntervalSeconds = boss.getAttackIntervalSeconds();
        long valorsetRewardBoss = boss.getRewardBoss();

        // Limitar HP
        if (valorHpMax < MAX_HP) {

            boss.setMaxHp(valorHpMax + incrementarUp);
            boss.setCurrentHp(valorCur + incrementarUp);

        } else {

            boss.setMaxHp(MAX_HP);
            boss.setCurrentHp(MAX_HP);
        }

        // Limitar recompensa boss
        if (valorsetRewardBoss < MAX_REWARD_BOSS) {

            boss.setRewardBoss(valorsetRewardBoss + 1);

        } else {

            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        // Limitar XP
        long valorXp = boss.getRewardExp();

        if (valorXp < MAX_EXP) {

            boss.setRewardExp(valorXp + 1);

        } else {

            boss.setRewardExp(MAX_EXP);
        }

        // Evolução do ataque
        if (valorAtaque < MAX_ATTACK) {

            boss.setAttackPower(valorAtaque + 1);

        } else {

            boss.setAttackPower(MAX_ATTACK);
        }

        // Evolução intervalo de ataque
        if (valorIntervalSeconds < MAX_INTERVAL) {

            boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);

        } else {

            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }
    }
}