package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossDestruidor;
import com.boss_battle.repository.DestruidorRepository;

import jakarta.transaction.Transactional;



@Service
@Transactional
public class DestruidorService  {
	private static final long MAX_ATTACK = 3500;
	private static final long MAX_INTERVAL = 1500;
	private static final long MAX_REWARD_BOSS = 1_300_000;
	private static final long MAX_EXP = 16000;
	private static final long MAX_HP = 10_000_000;
    @Autowired
    private DestruidorRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossDestruidor get() {
    	  return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    // Cria o boss padrão
    public GlobalBossDestruidor createDefaultBoss() {
    	GlobalBossDestruidor boss = new GlobalBossDestruidor();

        boss.setName("DESTRUIDOR");
        aplicarEscalamentoDestruidor(boss);
        
       // boss.setMaxHp(200_000L);
        //boss.setCurrentHp(200_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
        
       
 
       // boss.setAttackPower(400L);
       // boss.setAttackIntervalSeconds(180L);

        boss.setImageUrl("images/boss_destruidor.webp");

        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(14_400L); 
        boss.setSpawnCount(1);

       // boss.setRewardBoss(100_000L);
       // boss.setRewardExp(5_000L);

        return repo.save(boss);
    }

    public GlobalBossDestruidor save(GlobalBossDestruidor boss) {
        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss morrer/ se der certo..
    //===========================================================
    public void aplicarEscalamentoDestruidor(GlobalBossDestruidor boss) {


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

    }
    
   

    // Ataque direto (usado em auto-attack ou debug)
    public GlobalBossDestruidor attack(long damage) {
    	GlobalBossDestruidor boss = get();
        boss.applyDamage(damage);
        return repo.save(boss);
    }
}


