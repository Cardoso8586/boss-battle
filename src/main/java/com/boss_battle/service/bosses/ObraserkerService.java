package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossObraserker;
import com.boss_battle.repository.ObraserkerRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ObraserkerService {

    private static final long MAX_ATTACK = 500;              // ajuste se quiser
    private static final long MAX_INTERVAL = 200;            // ajuste se quiser
    private static final long MAX_REWARD_BOSS = 300_000;     // ajuste se quiser
    private static final long MAX_EXP = 50_000;              // ajuste se quiser
    private static final long MAX_HP = 550_000;              // ajuste se quiser

    @Autowired
    private ObraserkerRepository repo;

    // =============================
    // GET BOSS ÚNICO
    // =============================
    public GlobalBossObraserker get() {
        return repo.findById(1L)
                .orElseGet(() -> createDefaultBoss());
    }

    // =============================
    // CRIAR BOSS PADRÃO
    // =============================
    public GlobalBossObraserker createDefaultBoss() {

        GlobalBossObraserker boss = new GlobalBossObraserker();

        boss.setName("OBRASERKER, GIGANTE DE AÇO");

        aplicarEscalamentoObraserker(boss);

        boss.setProcessingDeath(false);
        boss.setAlive(true);

        boss.setImageUrl("images/boss_obraserker.webp");

        boss.setSpawnedAt(LocalDateTime.now());

        // respawn 4 horas
        boss.setRespawnCooldownSeconds(14_400L);

        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossObraserker save(GlobalBossObraserker boss) {
        return repo.save(boss);
    }

    // ===========================================================
    // ESCALAMENTO DO BOSS
    // ===========================================================
    public void aplicarEscalamentoObraserker(GlobalBossObraserker boss) {

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
            boss.setRewardBoss(valorRewardBoss + 2);
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
    public GlobalBossObraserker attack(long damage) {

        GlobalBossObraserker boss = get();

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}