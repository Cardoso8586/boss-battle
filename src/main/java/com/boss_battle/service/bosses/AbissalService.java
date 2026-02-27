package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossAbissal;
import com.boss_battle.repository.AbissalRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AbissalService {

    private static final long MAX_ATTACK = 1500;
    private static final long MAX_INTERVAL = 1500;
    private static final long MAX_REWARD_BOSS = 2_000_000;
    private static final long MAX_EXP = 30_000;
    private static final long MAX_HP = 15_000_000;

    @Autowired
    private AbissalRepository repo;

    // =============================
    // GET BOSS
    // =============================
    public GlobalBossAbissal get() {
        return repo.findById(1L)
                .orElseGet(() -> createDefaultBoss());
    }

    // =============================
    // CREATE DEFAULT BOSS
    // =============================
    public GlobalBossAbissal createDefaultBoss() {

        GlobalBossAbissal boss = new GlobalBossAbissal();

        boss.setName("SOBERANO ABISSAL");

        boss.setMaxHp(100_000L);
        boss.setCurrentHp(100_000L);

        boss.setProcessingDeath(false);
        boss.setAlive(true);

        boss.setImageUrl("images/boss_abissal.webp");

        boss.setSpawnedAt(LocalDateTime.now());

        // Respawn 5h
        boss.setRespawnCooldownSeconds(18_000L);

        boss.setSpawnCount(1);

        boss.setRewardBoss(100_000L);
        boss.setRewardExp(20_000L);

        return repo.save(boss);
    }

    public GlobalBossAbissal save(GlobalBossAbissal boss) {
        return repo.save(boss);
    }

    // =============================
    // ATAQUE
    // =============================
    public GlobalBossAbissal attack(long damage) {

        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss Abissal not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }

    // ===========================================================
    // ESCALAMENTO DO ABISSAL
    // ===========================================================
    public void aplicarEscalamentoAbissal(GlobalBossAbissal boss) {

        Random random = new Random();

        long min = 30;
        long max = 200;

        long incrementarUp = random.nextLong(min, max + 1);

        long valorHpMax = boss.getMaxHp();
        long valorCur = boss.getCurrentHp();
        long valorAtaque = boss.getAttackPower();
        long valorIntervalSeconds = boss.getAttackIntervalSeconds();
        long valorRewardBoss = boss.getRewardBoss();

        // ================= HP =================
        if (valorHpMax < MAX_HP) {
            boss.setMaxHp(valorHpMax + incrementarUp);
            boss.setCurrentHp(valorCur + incrementarUp);
        } else {
            boss.setMaxHp(MAX_HP);
            boss.setCurrentHp(MAX_HP);
        }

        // ================= REWARD =================
        if (valorRewardBoss < MAX_REWARD_BOSS) {
            boss.setRewardBoss(valorRewardBoss + 2);
        } else {
            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        // ================= EXP =================
        long valorXp = boss.getRewardExp();

        if (valorXp < MAX_EXP) {
            boss.setRewardExp(valorXp + 2);
        } else {
            boss.setRewardExp(MAX_EXP);
        }

        // ================= ATAQUE =================
        if (valorAtaque < MAX_ATTACK) {
            boss.setAttackPower(valorAtaque + 2);
        } else {
            boss.setAttackPower(MAX_ATTACK);
        }

        // ================= INTERVALO =================
        if (valorIntervalSeconds < MAX_INTERVAL) {
            boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);
        } else {
            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }
    }
}