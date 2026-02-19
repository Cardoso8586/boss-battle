package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossFlamor;
import com.boss_battle.repository.FlamorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FlamorService {

    @Autowired
    private FlamorRepository repo;

    public GlobalBossFlamor get() {
    	  return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossFlamor createDefaultBoss() {
    	GlobalBossFlamor boss = new GlobalBossFlamor();
         boss.setName("FLAMOR");
         boss.setMaxHp(250_000L);
         boss.setCurrentHp(250_000L);
         boss.setProcessingDeath(false);
         boss.setAlive(true);
         boss.setImageUrl("images/boss_flamor.webp");
         boss.setSpawnedAt(LocalDateTime.now());
         boss.setRespawnCooldownSeconds(3600L);
         boss.setSpawnCount(1);
        // boss.setAttackIntervalSeconds(146L);
        // boss.setAttackPower(100L);
         boss.setRewardBoss(80_000L);
         boss.setRewardExp(2500L);
        return repo.save(boss);
    }

    public GlobalBossFlamor save(GlobalBossFlamor boss) {
        return repo.save(boss);
    }
    
    public GlobalBossFlamor attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss morrer
    //===========================================================
    public void aplicarEscalamentoFlamor(GlobalBossFlamor boss) {


        Random random = new Random();
    	long min = 50;
    	long max = 200;
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
 	   
    }//--->incrmentar hp, toda vez que o boss morrer

   

}
