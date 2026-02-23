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

	private static final long MAX_ATTACK = 5000;
	private static final long MAX_INTERVAL = 1600;
	private static final long MAX_REWARD_BOSS = 1_500_000;
	private static final long MAX_EXP = 22000;
	private static final long MAX_HP = 16_000_000;
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
       // boss.setAttackPower(420L);
        //boss.setAttackIntervalSeconds(190L);
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
                boss.setAttackPower(valorAtaque + 5);
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
