package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossNecrothar;
import com.boss_battle.repository.NecrotharRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NecrotharService {

    private static final long MAX_ATTACK = 1500;
    private static final long MAX_INTERVAL = 1000;
    private static final long MAX_REWARD_BOSS = 1_100_000;
    private static final long MAX_EXP = 19000;
    private static final long MAX_HP = 12_000_000;

    @Autowired
    private NecrotharRepository repo;

    // ===========================================================
    // RETORNA BOSS ÚNICO
    // ===========================================================
    public GlobalBossNecrothar get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    // ===========================================================
    // CRIAR BOSS PADRÃO
    // ===========================================================
    public GlobalBossNecrothar createDefaultBoss() {

        GlobalBossNecrothar boss = new GlobalBossNecrothar();

        boss.setName("NECROTHAR, TIRANO DO ABISMO");
        boss.setMaxHp(640_000L);
        boss.setCurrentHp(640_000L);

        boss.setProcessingDeath(false);
        boss.setAlive(true);

        boss.setImageUrl(
            "images/boss_evolution/boss_necrothar/necrothar_phase1.webp");

        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(19_800L);
        boss.setSpawnCount(1);

        boss.setRewardBoss(260_000L);
        boss.setRewardExp(18_000L);

        return repo.save(boss);
    }

    public GlobalBossNecrothar save(GlobalBossNecrothar boss) {
        return repo.save(boss);
    }

    // ===========================================================
    // ESCALAMENTO AO MORRER
    // ===========================================================
    public void aplicarEscalamentoNecrothar(GlobalBossNecrothar boss) {

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
            boss.setAttackPower(ataque + 4);
        } else {
            boss.setAttackPower(MAX_ATTACK);
        }

        // ===== INTERVALO =====
        if (interval < MAX_INTERVAL) {
            boss.setAttackIntervalSeconds(interval + 1);
        } else {
            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }

        evoluirNecrothar(boss);
    }

    // ===========================================================
    // EVOLUÇÃO
    // ===========================================================
    public void evoluirNecrothar(GlobalBossNecrothar boss) {

        long hp = boss.getMaxHp();

        if (hp < 750_000L) {

            boss.setName("NECROTHAR, TIRANO DO ABISMO");
            boss.setImageUrl(
                "images/boss_evolution/boss_necrothar/necrothar_phase1.webp");

        

        } else {

            boss.setName("NECROTHAR, IMPERADOR SEM FIM");
            boss.setImageUrl(
                "images/boss_evolution/boss_necrothar/necrothar_phase2.webp");
        }
    }

    // ===========================================================
    // ATAQUE
    // ===========================================================
    public GlobalBossNecrothar attack(long damage) {

        GlobalBossNecrothar boss = get();
        boss.applyDamage(damage);

        return repo.save(boss);
    }
}