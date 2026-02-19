package com.boss_battle.service.bosses;



import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossNoxar;
import com.boss_battle.repository.NoxarRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NoxarService {

    @Autowired
    private NoxarRepository repo;

    public GlobalBossNoxar get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossNoxar createDefaultBoss() {
    	GlobalBossNoxar boss = new GlobalBossNoxar();
         boss.setName("NOXAR");
         boss.setMaxHp(50_000L);
         boss.setCurrentHp(50_000L);
         boss.setProcessingDeath(false);
         boss.setAlive(true);
         boss.setImageUrl("images/boss_noxar.webp");
         boss.setSpawnedAt(LocalDateTime.now());
         boss.setRespawnCooldownSeconds(5400L);
         boss.setSpawnCount(1);
        // boss.setAttackIntervalSeconds(160L);
        // boss.setAttackPower(180L);
         boss.setRewardBoss(50_000L);
         boss.setRewardExp(2200L);
        return repo.save(boss);
    }

    public GlobalBossNoxar save(GlobalBossNoxar boss) {
        return repo.save(boss);
    }
    
    public GlobalBossNoxar attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }

    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoNoxar(GlobalBossNoxar boss) {


        Random random = new Random();
    	long min = 10;
    	long max = 100;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 1);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 1);
 	   
    	 //ataque respaw
        boss.setAttackPower(boss.getAttackPower() + 2);
        boss.setAttackIntervalSeconds(boss.getAttackIntervalSeconds() + 1);
    }//--->incrmentar hp, toda vez que o boss for derrotado

}
