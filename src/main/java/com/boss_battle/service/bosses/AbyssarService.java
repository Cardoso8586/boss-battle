package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossAbyssar;
import com.boss_battle.repository.AbyssarRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AbyssarService {

    @Autowired
    private AbyssarRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossAbyssar get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    // Cria o boss padrão
    public GlobalBossAbyssar createDefaultBoss() {

        GlobalBossAbyssar boss = new GlobalBossAbyssar();

        boss.setName("ABYSSAR DOMINATOR");
        boss.setMaxHp(520_000L);
        boss.setCurrentHp(520_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);

        //boss.setAttackPower(400L);
        //boss.setAttackIntervalSeconds(190L);

        boss.setImageUrl("images/boss_evolution/boss_abyssar/boss_abyssar.webp");

        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(18_000L);
        boss.setSpawnCount(1);

        boss.setRewardBoss(210_000L);
        boss.setRewardExp(14_000L);

        return repo.save(boss);
    }

    public GlobalBossAbyssar save(GlobalBossAbyssar boss) {
        return repo.save(boss);
    }

    //===========================================================
    // ESCALAMENTO AO MORRER
    //===========================================================
    public void aplicarEscalamentoAbyssar(GlobalBossAbyssar boss) {

        Random random = new Random();

        long min = 50;
        long max = 250;
        long incrementarUp = random.nextLong(min, max + 1);

        long valorHpMax = boss.getMaxHp();
        long valorCur = boss.getCurrentHp();

        //hp
        boss.setMaxHp(valorHpMax + incrementarUp);
        boss.setCurrentHp(valorCur + incrementarUp);
        //recompensa
        boss.setRewardExp(boss.getRewardExp() + 2);
        boss.setRewardBoss(boss.getRewardBoss() + 2);

   	    //ataque respaw
     	long valorAtaque = boss.getAttackPower();
   	    boss.setAttackPower(valorAtaque+ 5);
     	long valorIntervalSeconds= boss.getAttackIntervalSeconds();
        boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);
        
        evoluirAbyssar(boss);
    }

 // ================== EVOLUÇÃO ==================
    public void evoluirAbyssar(GlobalBossAbyssar boss) {

        if (boss.getMaxHp() < 600_000L) {

            boss.setName("ABYSSAR DOMINADOR");
            boss.setImageUrl("images/boss_evolution/boss_abyssar/boss_abyssar.webp");

        } else if (boss.getMaxHp() < 850_000L) {

            boss.setName("ABYSSAR, TIRANO DO VAZIO");
            boss.setImageUrl("images/boss_evolution/boss_abyssar/boss_abyssar_void.webp");

            boss.setAttackPower(4_200L);

        } else if (boss.getMaxHp() < 1_200_000L) {

            boss.setName("ABYSSAR, MONARCA ABISSAL");
            boss.setImageUrl("images/boss_evolution/boss_abyssar/boss_abyssar_monarch.webp");

            boss.setAttackPower(5_600L);

        } else {

            boss.setName("ABYSSAR, IMPERADOR DO ABISMO ETERNO");
            boss.setImageUrl("images/boss_evolution/boss_abyssar/boss_abyssar_emperor.webp");

            boss.setAttackPower(7_500L);
            boss.setAttackIntervalSeconds(12L);
        }
    }


    // Ataque direto
    public GlobalBossAbyssar attack(long damage) {

        GlobalBossAbyssar boss = get();
        boss.applyDamage(damage);

        return repo.save(boss);
    }
}
