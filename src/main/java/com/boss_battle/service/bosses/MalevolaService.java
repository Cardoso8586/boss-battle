package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossMalevola;
import com.boss_battle.repository.MalevolaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MalevolaService {

    private static final long MAX_ATTACK = 500;           // ajuste se quiser
    private static final long MAX_INTERVAL = 1000;        // ajuste se quiser
    private static final long MAX_REWARD_BOSS = 500_000;  // ajuste se quiser
    private static final long MAX_EXP = 79_000;           // ajuste se quiser
    private static final long MAX_HP = 950_000;           // ajuste se quiser

    @Autowired
    private MalevolaRepository repo;

    // ===========================================================
    // RETORNA BOSS ÚNICO
    // ===========================================================
    public GlobalBossMalevola get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    // ===========================================================
    // CRIAR BOSS PADRÃO
    // ===========================================================
    public GlobalBossMalevola createDefaultBoss() {

        GlobalBossMalevola boss = new GlobalBossMalevola();

        boss.setName("MALEVOLA, SENHORA DAS TREVAS");

        boss.setProcessingDeath(false);
        boss.setAlive(true);

        boss.setImageUrl("images/boss_malevola.webp");

        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(9_000L);  // respawn 2h30
        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossMalevola save(GlobalBossMalevola boss) {
        return repo.save(boss);
    }

    // ===========================================================
    // ESCALAMENTO AO MORRER
    // ===========================================================
    public void aplicarEscalamentoMalevola(GlobalBossMalevola boss) {

        Random random = new Random();

        long incrementarUp = random.nextLong(15, 120);

        long hpMax = boss.getMaxHp();
        long hpCur = boss.getCurrentHp();
        long ataque = boss.getAttackPower();
        long interval = boss.getAttackIntervalSeconds();
        long rewardBoss = boss.getRewardBoss();

        // ===== HP =====
        if (hpMax < MAX_HP) {
            boss.setMaxHp(hpMax + incrementarUp);
            boss.setCurrentHp(hpCur + incrementarUp);
        } else {
            boss.setMaxHp(MAX_HP);
            boss.setCurrentHp(MAX_HP);
        }

        // ===== REWARD =====
        if (rewardBoss < MAX_REWARD_BOSS) {
            boss.setRewardBoss(rewardBoss + 2);
        } else {
            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        // ===== EXP =====
        long xp = boss.getRewardExp();
        if (xp < MAX_EXP) {
            boss.setRewardExp(xp + 2);
        } else {
            boss.setRewardExp(MAX_EXP);
        }

        // ===== ATAQUE =====
        if (ataque < MAX_ATTACK) {
            boss.setAttackPower(ataque + 1);
        } else {
            boss.setAttackPower(MAX_ATTACK);
        }

        // ===== INTERVALO =====
        if (interval < MAX_INTERVAL) {
            boss.setAttackIntervalSeconds(interval + 1);
        } else {
            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }

       
    }

  
    // ===========================================================
    // ATAQUE
    // ===========================================================
    public GlobalBossMalevola attack(long damage) {

        GlobalBossMalevola boss = get();
        boss.applyDamage(damage);

        return repo.save(boss);
    }
}