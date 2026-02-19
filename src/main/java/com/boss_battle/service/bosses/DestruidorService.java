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
    	
    	boss.setMaxHp( valorHpMax + incrementarUp);
    	boss.setCurrentHp( valorCur + incrementarUp);
    	
    	long valorXp =  boss.getRewardExp();
    	boss.setRewardExp(valorXp + 1);
    	
    	long valorsetRewardBoss = boss.getRewardBoss();
 	    boss.setRewardBoss(valorsetRewardBoss + 1);
 	   
 	   //ataque respaw
     	long valorAtaque = boss.getAttackPower();
     	boss.setAttackPower(valorAtaque+ 6);
    	long valorIntervalSeconds= boss.getAttackIntervalSeconds();
        boss.setAttackIntervalSeconds(valorIntervalSeconds + 1);
        
    }
    
   

    // Ataque direto (usado em auto-attack ou debug)
    public GlobalBossDestruidor attack(long damage) {
    	GlobalBossDestruidor boss = get();
        boss.applyDamage(damage);
        return repo.save(boss);
    }
}


