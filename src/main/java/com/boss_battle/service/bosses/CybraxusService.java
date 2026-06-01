package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossCybraxus;
import com.boss_battle.repository.CybraxusRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CybraxusService {

    private static final long MAX_ATTACK = 1_200L;
    private static final long MAX_INTERVAL = 600L;
    private static final long MAX_REWARD_BOSS = 300_000L;
    private static final long MAX_EXP = 50_000L;
    private static final long MAX_HP = 500_000L;

    @Autowired
    private CybraxusRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossCybraxus get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    // Cria o boss padrão
    public GlobalBossCybraxus createDefaultBoss() {

        GlobalBossCybraxus boss = new GlobalBossCybraxus();

        boss.setName("CYBRAXUS");

        aplicarEscalamentoCybraxus(boss);

        boss.setProcessingDeath(false);
        boss.setRewardDistributed(false);
        boss.setAlive(true);

        boss.setImageUrl("images/boss_cybraxus.webp");

        boss.setSpawnedAt(LocalDateTime.now());

        // 6 horas
        boss.setRespawnCooldownSeconds(21_600L);

        boss.setSpawnCount(1);

        return repo.save(boss);
    }

    public GlobalBossCybraxus save(GlobalBossCybraxus boss) {
        return repo.save(boss);
    }

    // ===========================================================
    // Incrementa atributos toda vez que o boss evoluir/morrer
    // ===========================================================
    public void aplicarEscalamentoCybraxus(GlobalBossCybraxus boss) {

        Random random = new Random();

        long min = 100L;
        long max = 500L;
        long incrementarHp = random.nextLong(min, max + 1);

        long valorHpMax = boss.getMaxHp();
        long valorCur = boss.getCurrentHp();
        long valorAtaque = boss.getAttackPower();
        long valorIntervalSeconds = boss.getAttackIntervalSeconds();
        long valorRewardBoss = boss.getRewardBoss();
        long valorXp = boss.getRewardExp();

        // Limitar HP
        if (valorHpMax < MAX_HP) {
            boss.setMaxHp(valorHpMax + incrementarHp);
            boss.setCurrentHp(valorCur + incrementarHp);
        } else {
            boss.setMaxHp(MAX_HP);
            boss.setCurrentHp(MAX_HP);
        }

        // Limitar recompensa
        if (valorRewardBoss < MAX_REWARD_BOSS) {
            boss.setRewardBoss(valorRewardBoss + 10L);
        } else {
            boss.setRewardBoss(MAX_REWARD_BOSS);
        }

        // Limitar XP
        if (valorXp < MAX_EXP) {
            boss.setRewardExp(valorXp + 25L);
        } else {
            boss.setRewardExp(MAX_EXP);
        }

        // Limitar evolução do ataque
        if (valorAtaque < MAX_ATTACK) {
            boss.setAttackPower(valorAtaque + 15L);
        } else {
            boss.setAttackPower(MAX_ATTACK);
        }

        // Limitar evolução do intervalo
        if (valorIntervalSeconds < MAX_INTERVAL) {
            boss.setAttackIntervalSeconds(valorIntervalSeconds + 1L);
        } else {
            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }
    }

    // Ataque direto, usado em auto-attack ou debug
    public GlobalBossCybraxus attack(long damage) {

        GlobalBossCybraxus boss = get();

        boss.applyDamage(damage);

        return repo.save(boss);
    }
}