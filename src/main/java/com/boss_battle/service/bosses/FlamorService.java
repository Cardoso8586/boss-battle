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

	private static final long MAX_ATTACK = 3700;
	private static final long MAX_INTERVAL = 1100;
	private static final long MAX_REWARD_BOSS = 1_300_000;
	private static final long MAX_EXP = 18000;
	private static final long MAX_HP = 9_000_000;
	
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
              boss.setAttackPower(valorAtaque + 2);
          } else {
              boss.setAttackPower(MAX_ATTACK);
          }

          // Limitar Evolução do intervalo
          if (valorIntervalSeconds < MAX_INTERVAL) {
              boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);
          } else {
              boss.setAttackIntervalSeconds(MAX_INTERVAL);
          }

 	   
    }//--->incrmentar hp, toda vez que o boss morrer

   

}
