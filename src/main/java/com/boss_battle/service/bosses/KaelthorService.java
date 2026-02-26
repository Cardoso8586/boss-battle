package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossKaelthor;
import com.boss_battle.repository.KaelthorRepository;


import jakarta.transaction.Transactional;

@Service
@Transactional
public class KaelthorService {

    private static final long MAX_ATTACK = 3_100;
    private static final long MAX_INTERVAL = 1800; 
    private static final long MAX_REWARD_BOSS = 2_000_000;
    private static final long MAX_EXP = 25000;
    private static final long MAX_HP = 15_000_000;

    @Autowired
    private KaelthorRepository repo;

    // =============================
    // GET BOSS ÚNICO
    // =============================
    public GlobalBossKaelthor get() {
        return repo.findById(1L)
                .orElseGet(() -> createDefaultBoss());
    }

    // =============================
    // CRIAR BOSS PADRÃO
    // =============================
    public GlobalBossKaelthor createDefaultBoss() {

    	GlobalBossKaelthor boss = new GlobalBossKaelthor();

        boss.setName("KAELTHOR");

        aplicarEscalamentoTempestade(boss);

        boss.setProcessingDeath(false);
        boss.setAlive(true);

        boss.setImageUrl("images/boss_kaelthor.webp");

        boss.setSpawnedAt(LocalDateTime.now());

        // respawn 2 horas
        boss.setRespawnCooldownSeconds(7_200L);

        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossKaelthor save(GlobalBossKaelthor boss) {
        return repo.save(boss);
    }

    // ===========================================================
    // ESCALAMENTO DO BOSS TEMPESTADE
    // ===========================================================
    public void aplicarEscalamentoTempestade(GlobalBossKaelthor boss) {

        Random random = new Random();

        long min = 20;
        long max = 150;

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
            boss.setAttackPower(valorAtaque + 1);
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

    // =============================
    // ATAQUE DIRETO
    // =============================
    public GlobalBossKaelthor attack(long damage) {

    	GlobalBossKaelthor boss = get();

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}