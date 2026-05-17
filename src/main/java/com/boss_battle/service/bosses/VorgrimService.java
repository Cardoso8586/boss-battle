package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossVorgrim;
import com.boss_battle.repository.VorgrimRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VorgrimService {

    private static final long MAX_ATTACK = 1800;
    private static final long MAX_INTERVAL = 1000;
    private static final long MAX_REWARD_BOSS = 5000_000;
    private static final long MAX_EXP = 150_000;
    private static final long MAX_HP = 3_000_000;

    @Autowired
    private VorgrimRepository repo;

    public GlobalBossVorgrim get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossVorgrim createDefaultBoss() {

        GlobalBossVorgrim boss = new GlobalBossVorgrim();

        boss.setName("VORGRIM");
        boss.setProcessingDeath(false);
        boss.setRewardDistributed(false);
        boss.setAlive(true);

        boss.setImageUrl("images/boss_vorgrim.webp");

        boss.setSpawnedAt(LocalDateTime.now());

        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossVorgrim save(GlobalBossVorgrim boss) {
        return repo.save(boss);
    }

    public GlobalBossVorgrim attack(long damage) {

        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }

    //===========================================================
    // incrementar hp toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoVorgrim(GlobalBossVorgrim boss) {

        Random random = new Random();

        long min = 100;
        long max = 300;

        long incrementarUp = random.nextLong(min, max + 1);

        long valorHpMax = boss.getMaxHp();
        long valorCur = boss.getCurrentHp();
        long valorAtaque = boss.getAttackPower();
        long valorIntervalSeconds = boss.getAttackIntervalSeconds();
        long valorsetRewardBoss = boss.getRewardBoss();

        //===========================================================
        // HP
        //===========================================================
        if (valorHpMax < MAX_HP) {

            boss.setMaxHp(valorHpMax + incrementarUp);
            boss.setCurrentHp(valorCur + incrementarUp);

        } else {

            boss.setMaxHp(MAX_HP);
            boss.setCurrentHp(MAX_HP);
        }

        //===========================================================
        // recompensa boss
        //===========================================================
        if (valorsetRewardBoss < MAX_REWARD_BOSS) {

            boss.setRewardBoss(valorsetRewardBoss + 1);

        } else {

            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        //===========================================================
        // XP
        //===========================================================
        long valorXp = boss.getRewardExp();

        if (valorXp < MAX_EXP) {

            boss.setRewardExp(valorXp + 1);

        } else {

            boss.setRewardExp(MAX_EXP);
        }

        //===========================================================
        // ataque
        //===========================================================
        if (valorAtaque < MAX_ATTACK) {

            boss.setAttackPower(valorAtaque + 1);

        } else {

            boss.setAttackPower(MAX_ATTACK);
        }

        //===========================================================
        // intervalo ataque
        //===========================================================
        if (valorIntervalSeconds < MAX_INTERVAL) {

            boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);

        } else {

            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }
    }
}