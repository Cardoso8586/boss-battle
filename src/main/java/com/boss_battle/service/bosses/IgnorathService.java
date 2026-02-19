package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossIgnorath;
import com.boss_battle.repository.IgnorathRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class IgnorathService {

    @Autowired
    private IgnorathRepository repo;

    public GlobalBossIgnorath get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossIgnorath createDefaultBoss() {
    	 GlobalBossIgnorath boss = new GlobalBossIgnorath();
         boss.setName("IGNORATH");
         boss.setMaxHp(150_000);
         boss.setCurrentHp(150_000);
         boss.setProcessingDeath(false);
         boss.setAlive(true);
         boss.setImageUrl("images/boss_ignorath.webp");
         boss.setSpawnedAt(LocalDateTime.now());
         boss.setRespawnCooldownSeconds(3600);
         boss.setSpawnCount(1);
         //boss.setAttackIntervalSeconds(166L);
        // boss.setAttackPower(75L);
         boss.setRewardBoss(75_000);
         boss.setRewardExp(3000);
        return repo.save(boss);
    }

    public GlobalBossIgnorath save(GlobalBossIgnorath boss) {
        return repo.save(boss);
    }
    
    public GlobalBossIgnorath attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }
    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoIgnorath(GlobalBossIgnorath boss) {


        Random random = new Random();
    	long min = 100;
    	long max = 300;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 2);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 2);
 	   
    	 //ataque respaw
        boss.setAttackPower(boss.getAttackPower() + 2);
        boss.setAttackIntervalSeconds(boss.getAttackIntervalSeconds() + 1);
    }//--->incrmentar hp, toda vez que o boss for derrotado
   

}
