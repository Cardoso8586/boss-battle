package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossTrigonBaphydrax;
import com.boss_battle.repository.TrigonBaphydraxRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TrigonBaphydraxService {

    @Autowired
    private TrigonBaphydraxRepository repo;

    public GlobalBossTrigonBaphydrax get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    
    public GlobalBossTrigonBaphydrax createDefaultBoss() {
    	GlobalBossTrigonBaphydrax boss = new GlobalBossTrigonBaphydrax();
        boss.setName("TRÍGON BAPHYDRAX");
        boss.setMaxHp(280_000);
        boss.setCurrentHp(280_000);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        boss.setImageUrl("images/boss_trigon_baphydrax.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(7200);
        boss.setSpawnCount(1);
        boss.setAttackPower(180L);
        boss.setAttackIntervalSeconds(185L);
        boss.setRewardBoss(75_000); 
        boss.setRewardExp(3_500);

        return repo.save(boss);
    }

    public GlobalBossTrigonBaphydrax save(GlobalBossTrigonBaphydrax boss) {
        return repo.save(boss);
    }

    public GlobalBossTrigonBaphydrax attack(long damage) {
        var boss = repo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss);
    }//=============================================================
    
    public void aplicarEscalamentoTrigon(GlobalBossTrigonBaphydrax boss) {


        Random random = new Random();
    	long min = 80;
    	long max = 260;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 3);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 5);
    	
    	 //ataque respaw
        boss.setAttackPower(boss.getAttackPower() + 2);
        boss.setAttackIntervalSeconds(boss.getAttackIntervalSeconds() + 1);
    
 	   
    }//--->incrmentar hp, toda vez que o boss morrer
}