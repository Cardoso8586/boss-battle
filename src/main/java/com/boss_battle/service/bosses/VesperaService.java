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

	private static final long MAX_ATTACK = 1500;
	private static final long MAX_INTERVAL = 600;
	private static final long MAX_REWARD_BOSS = 900_000;
	private static final long MAX_EXP = 15000;
	private static final long MAX_HP = 8_000_000;
	
	
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
         //boss.setAttackIntervalSeconds(170L);
         //boss.setAttackPower(140L);
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
    	long valorAtaque = boss.getAttackPower();
        long valorIntervalSeconds = boss.getAttackIntervalSeconds();
        long valorsetRewardBoss = boss.getRewardBoss();
        
        
        //Limitar hp
        if(valorHpMax < MAX_HP) {
        	boss.setMaxHp( valorHpMax + incrementarUp);
        	boss.setCurrentHp( valorCur + incrementarUp);
    	
        }else {
        	
        	boss.setMaxHp(MAX_HP);
        	boss.setCurrentHp(MAX_HP);
        }
    	
    	//---> Limitar recompensa
    	
        if(valorsetRewardBoss < MAX_REWARD_BOSS) {
        	
        	boss.setRewardBoss(valorsetRewardBoss + 1);
        }else {
        	
        	boss.setRewardBoss(MAX_REWARD_BOSS);
        }
   
        //--->Limitar xp
 	    long valorXp =  boss.getRewardExp();
        if(valorXp < MAX_EXP) {
           boss.setRewardExp(valorXp + 1);
        }else {
        	 boss.setRewardExp(MAX_EXP);
        	
        }
        
        // Limitar Evolução do ataque
        if (valorAtaque < MAX_ATTACK) {
            boss.setAttackPower(valorAtaque + 3);
        } else {
            boss.setAttackPower(MAX_ATTACK);
        }

        // Limitar Evolução do intervalo
        if (valorIntervalSeconds < MAX_INTERVAL) {
            boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);
        } else {
            boss.setAttackIntervalSeconds(MAX_INTERVAL);
        }
    }//--->incrmentar hp, toda vez que o boss for derrotado


}