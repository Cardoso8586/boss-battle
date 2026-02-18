package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossInfernax;
import com.boss_battle.repository.InfernaxRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class InfernaxService {

    @Autowired
    private InfernaxRepository repo;

    public GlobalBossInfernax get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossInfernax createDefaultBoss() {
    	GlobalBossInfernax boss = new GlobalBossInfernax();
        boss.setName("INFERNAX");
        boss.setMaxHp(320_000L);
        boss.setCurrentHp(320_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_infernax.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(7200); 
        boss.setSpawnCount(1);
        boss.setRewardBoss(180_000L);
        boss.setAttackPower(420L);
        boss.setAttackIntervalSeconds(190L);
        boss.setRewardExp(4200);
        return repo.save(boss);
    }

    public GlobalBossInfernax save(GlobalBossInfernax boss) {
        return repo.save(boss);
    }
    
    public GlobalBossInfernax attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoInfernax (GlobalBossInfernax boss) {


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
