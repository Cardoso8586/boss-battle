package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossUmbrar;
import com.boss_battle.repository.UmbrarRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UmbrarService {

	private static final long MAX_ATTACK = 1800;
	private static final long MAX_INTERVAL = 700;
	private static final long MAX_REWARD_BOSS = 900_000;
	private static final long MAX_EXP = 25000;
	private static final long MAX_HP = 8_000_000;
	
    @Autowired
    private UmbrarRepository repo;

    public GlobalBossUmbrar get() {
        return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    public GlobalBossUmbrar createDefaultBoss() {
    	GlobalBossUmbrar boss = new GlobalBossUmbrar();
         boss.setName("UMBRAR");
         boss.setMaxHp(42_000L);
         boss.setCurrentHp(42_000L);
         boss.setProcessingDeath(false);
         boss.setAlive(true);
         boss.setImageUrl("images/boss_umbrar.webp");
         boss.setSpawnedAt(LocalDateTime.now());
         boss.setRespawnCooldownSeconds(4500);
         boss.setSpawnCount(1);
         //boss.setAttackIntervalSeconds(150L);
         //boss.setAttackPower(130L);
         boss.setRewardBoss(40_000L);
         boss.setRewardExp(1800);
        return repo.save(boss);
    }

    public GlobalBossUmbrar save(GlobalBossUmbrar boss) {
        return repo.save(boss);
    }
    
    public GlobalBossUmbrar attack(long damage) {
        var boss = repo.findById(1L)
                       .orElseThrow(() -> new RuntimeException("Boss not found"));

        boss.applyDamage(damage);

        return repo.save(boss); // <- atualiza no banco
    }

    //===========================================================
    //incrmentar hp, toda vez que o boss for derrotado
    //===========================================================
    public void aplicarEscalamentoUmbrar(GlobalBossUmbrar boss) {



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
    }//--->incrmentar hp, toda vez que o boss for derrotado

}
