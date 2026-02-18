package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.GlobalBossVespera;
import com.boss_battle.repository.VesperaReposytory;

@Service
@Transactional
public class VesperaService {

    @Autowired
    private VesperaReposytory repo;

    public GlobalBossVespera get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossVespera createDefaultBoss() {
    	GlobalBossVespera boss = new GlobalBossVespera();
         boss.setName("VESPERA");
         boss.setMaxHp(48_000L);
         boss.setCurrentHp(48_000L);
         boss.setProcessingDeath(false);
         boss.setAlive(true);
         boss.setImageUrl("images/boss_vespera.webp");
         boss.setSpawnedAt(LocalDateTime.now());
         boss.setRespawnCooldownSeconds(3600L);
         boss.setSpawnCount(1);
         boss.setAttackIntervalSeconds(170L);
         boss.setAttackPower(140L);
         boss.setRewardBoss(45_000L);
         boss.setRewardExp(1900L);
        return repo.save(boss);
    }

    public GlobalBossVespera save(GlobalBossVespera boss) {
        return repo.save(boss);
    }
    
    public GlobalBossVespera attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }

    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoVespera(GlobalBossVespera boss) {


        Random random = new Random();
    	long min = 10;
    	long max = 100;
    	long incrementarUp = random.nextLong(min, max + 1);

    	
    	long valorHpMax =  boss.getMaxHp();
    	long valorCur = boss.getCurrentHp();
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 5);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
    	boss.setRewardBoss(valorsetRewardBoss + 3);
    	 //ataque respaw
        boss.setAttackPower(boss.getAttackPower() + 2);
        boss.setAttackIntervalSeconds(boss.getAttackIntervalSeconds() + 1);
 	   
    }//--->incrmentar hp, toda vez que o boss for derrotado


}