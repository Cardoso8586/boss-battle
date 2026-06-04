package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossVoidreaper;
import com.boss_battle.repository.VoidreaperRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VoidreaperService {

    private static final long MAX_ATTACK = 1_500;
    private static final long MAX_INTERVAL = 1100;
    private static final long MAX_REWARD_BOSS = 300_000;
    private static final long MAX_EXP = 149_000;
    private static final long MAX_HP = 700_000;

    @Autowired
    private VoidreaperRepository repo;

    public GlobalBossVoidreaper get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossVoidreaper createDefaultBoss() {
        GlobalBossVoidreaper boss = new GlobalBossVoidreaper();
        boss.setName("VOIDREAPER");
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_voidreaper.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(3600);
        boss.setSpawnCount(1);
        return repo.save(boss);
    }

    public GlobalBossVoidreaper save(GlobalBossVoidreaper boss) {
        return repo.save(boss);
    }

    public GlobalBossVoidreaper attack(long damage) {

        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }

    // ===========================================================
    // incrementar hp, toda vez que o boss for derrotado
    // ===========================================================
    public void aplicarEscalamentoVoidreaper(GlobalBossVoidreaper boss) {

        Random random = new Random();

        long min = 100;
        long max = 300;
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

        // Limitar recompensa
        if (valorsetRewardBoss < MAX_REWARD_BOSS) {

            boss.setRewardBoss(valorsetRewardBoss + 11);

        } else {

            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        // Limitar XP
        long valorXp = boss.getRewardExp();

        if (valorXp < MAX_EXP) {

            boss.setRewardExp(valorXp + 11);

        } else {

            boss.setRewardExp(MAX_EXP);
        }

        // Limitar evolução do ataque
        if (valorAtaque < MAX_ATTACK) {

            boss.setAttackPower(valorAtaque + 25);

        } else {

            boss.setAttackPower(MAX_ATTACK);
        }

        // Limitar evolução do intervalo
        if (valorIntervalSeconds < MAX_INTERVAL) {

            boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);

        } else {

            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }
    }
}