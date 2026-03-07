package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossLeviatanAbismo;
import com.boss_battle.repository.LeviatanAbismoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LeviatanAbismoService {

    private static final long MAX_ATTACK = 500;
    private static final long MAX_INTERVAL = 200; 
    private static final long MAX_REWARD_BOSS = 300_000;
    private static final long MAX_EXP = 50_000;
    private static final long MAX_HP = 550_000;

    @Autowired
    private LeviatanAbismoRepository repo;

    // =============================
    // GET BOSS ÚNICO
    // =============================
    public GlobalBossLeviatanAbismo get() {
        return repo.findById(1L)
                .orElseGet(() -> createDefaultBoss());
    }

    // =============================
    // CRIAR BOSS PADRÃO
    // =============================
    public GlobalBossLeviatanAbismo createDefaultBoss() {

        GlobalBossLeviatanAbismo boss = new GlobalBossLeviatanAbismo();

        boss.setName("LEVIATÃ DO ABISMO");

        aplicarEscalamentoLeviatanAbismo(boss);

        boss.setProcessingDeath(false);
        boss.setAlive(true);

        boss.setImageUrl("images/boss_leviatan_abismo.webp");

        boss.setSpawnedAt(LocalDateTime.now());

        // respawn 5 horas
        boss.setRespawnCooldownSeconds(18_000L);

        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossLeviatanAbismo save(GlobalBossLeviatanAbismo boss) {
        return repo.save(boss);
    }

    // ===========================================================
    // ESCALAMENTO DO BOSS
    // ===========================================================
    public void aplicarEscalamentoLeviatanAbismo(GlobalBossLeviatanAbismo boss) {

        Random random = new Random();

        long min = 10;
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
            boss.setRewardBoss(valorRewardBoss + 10);
        } else {
            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        // ================= EXP =================
        long valorXp = boss.getRewardExp();
        if (valorXp < MAX_EXP) {
            boss.setRewardExp(valorXp + 10);
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

    // =============================
    // ATAQUE DIRETO
    // =============================
    public GlobalBossLeviatanAbismo attack(long damage) {

        GlobalBossLeviatanAbismo boss = get();

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}