

package com.boss_battle.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.GlobalBossUmbraxis;
import com.boss_battle.repository.UmbraxisRepository;

@Service
@Transactional
public class UmbraxisService {

    @Autowired
    private UmbraxisRepository repo;

    public GlobalBossUmbraxis get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossUmbraxis createDefaultBoss() {
    	GlobalBossUmbraxis boss = new GlobalBossUmbraxis();
         boss.setName("UMBRA XIS");
        // boss.setMaxHp(200_000);
        // boss.setCurrentHp(200_000);
         boss.setAlive(true);
         boss.setImageUrl("images/boss_umbraxis.webp");
         boss.setSpawnedAt(LocalDateTime.now());
         boss.setRespawnCooldownSeconds(7200);
         boss.setSpawnCount(1);
         boss.setRewardBoss(100_000);
         boss.setRewardExp(2500);
        return repo.save(boss);
    }

    public GlobalBossUmbraxis save(GlobalBossUmbraxis boss) {
        return repo.save(boss);
    }
    
    public GlobalBossUmbraxis attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }

            //===========================================================
    	    //incrmentar hp, toda vez que o boss for derrotado
    	    //===========================================================
    	    public void aplicarEscalamentoUmbraxis(GlobalBossUmbraxis boss) {


    	        Random random = new Random();
    	    	long min = 50;
    	    	long max = 300;
    	    	long incrementarUp = random.nextLong(min, max + 1);

    	    	
    	    	long valorHpMax =  boss.getMaxHp();
    	    	long valorCur = boss.getCurrentHp();
    	    	
    	    	boss.setMaxHp( valorHpMax + incrementarUp);
    	    	boss.setCurrentHp( valorCur + incrementarUp);
    	    	
    	    	long valorXp =  boss.getRewardExp();
    	    	boss.setRewardExp(valorXp + 8);
    	    	
    	    	long valorsetRewardBoss = boss.getRewardBoss();
    	    	boss.setRewardBoss(valorsetRewardBoss + 8);
    	 	   
    	    }//--->incrmentar hp, toda vez que o boss for derrotado


}
