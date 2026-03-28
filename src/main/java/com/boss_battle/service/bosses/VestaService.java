package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossVesta;
import com.boss_battle.repository.VestaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VestaService {

    private static final long MAX_ATTACK = 600;           // ajuste se quiser
    private static final long MAX_INTERVAL = 1000;         // ajuste se quiser
    private static final long MAX_REWARD_BOSS = 550_000;   // ajuste se quiser
    private static final long MAX_EXP = 70_000;            // ajuste se quiser
    private static final long MAX_HP = 850_000;            // ajuste se quiser

    @Autowired
    private VestaRepository repo;

    // =============================
    // GET BOSS ÚNICO
    // =============================
    public GlobalBossVesta get() {
        return repo.findById(1L)
                .orElseGet(() -> createDefaultBoss());
    }

    // =============================
    // CRIAR BOSS PADRÃO
    // =============================
    public GlobalBossVesta createDefaultBoss() {

        GlobalBossVesta boss = new GlobalBossVesta();

        boss.setName("VESTA");

        aplicarEscalamentoVesta(boss);

        boss.setProcessingDeath(false);
        boss.setAlive(true);

        boss.setImageUrl("images/boss_vesta.webp");

        boss.setSpawnedAt(LocalDateTime.now());

        // respawn 2 horas e 30 minutos
        boss.setRespawnCooldownSeconds(9_000L);

        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossVesta save(GlobalBossVesta boss) {
        return repo.save(boss);
    }

    // ===========================================================
    // ESCALAMENTO DO BOSS TEMPESTADE (Vesta)
    // ===========================================================
    public void aplicarEscalamentoVesta(GlobalBossVesta boss) {

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
    public GlobalBossVesta attack(long damage) {

        GlobalBossVesta boss = get();

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}