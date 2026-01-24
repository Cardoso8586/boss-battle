package com.boss_battle.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossTenebris;
import com.boss_battle.repository.TenebrisRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TenebrisService {

    @Autowired
    private TenebrisRepository repo;

    public GlobalBossTenebris get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    
    public GlobalBossTenebris createDefaultBoss() {
    	GlobalBossTenebris boss = new GlobalBossTenebris();
        boss.setName("TENEBRIS");
        boss.setMaxHp(70_000);
        boss.setCurrentHp(70_000);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_tenebris.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(6600);
        boss.setSpawnCount(1);
        boss.setRewardBoss(65_000); 
        boss.setRewardExp(2500);

        return repo.save(boss);
    }

    public GlobalBossTenebris save(GlobalBossTenebris boss) {
        return repo.save(boss);
    }

    public GlobalBossTenebris attack(long damage) {
        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }
    
  //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoTenebris(GlobalBossTenebris boss) {


        Random random = new Random();
    	long min = 10;
    	long max = 300;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 5);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 5);
 	   
    }//--->incrmentar hp, toda vez que o boss for derrotado
}