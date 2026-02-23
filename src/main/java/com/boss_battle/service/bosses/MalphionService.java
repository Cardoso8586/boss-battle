package com.boss_battle.service.bosses;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boss_battle.model.GlobalBossMalphion;
import com.boss_battle.repository.MalphionRepository;

import jakarta.transaction.Transactional;



@Service
@Transactional
public class MalphionService  {
	private static final long MAX_ATTACK = 4000;
	private static final long MAX_INTERVAL = 1500;
	private static final long MAX_REWARD_BOSS = 1_000_000;
	private static final long MAX_EXP = 15000;
	private static final long MAX_HP = 15_000_000;
    @Autowired
    private MalphionRepository repo;

    // Sempre retorna o boss único (ID = 1)
    public GlobalBossMalphion get() {
    	  return repo.findById(1L).orElseGet(() -> createDefaultBoss());
    }

    // Cria o boss padrão
    public GlobalBossMalphion createDefaultBoss() {
    	GlobalBossMalphion boss = new GlobalBossMalphion();

        boss.setName("DESTRUIDOR");
        boss.setMaxHp(380_000L);
        boss.setCurrentHp(380_000L);
        boss.setProcessingDeath(false);
        boss.setAlive(true);
       // boss.setAttackPower(200L);
       // boss.setAttackIntervalSeconds(190L);
        boss.setImageUrl("images/boss_malphion.webp");
        boss.setSpawnedAt(LocalDateTime.now());
        boss.setRespawnCooldownSeconds(15_000L); 
        boss.setSpawnCount(1);
        
        boss.setRewardBoss(140_000L);
        boss.setRewardExp(9_000L);

        return repo.save(boss);
    }

    public GlobalBossMalphion save(GlobalBossMalphion boss) {
        return repo.save(boss);
    }
    
    //===========================================================
    //incrmentar hp, toda vez que o boss morrer/ se der certo..
    //===========================================================
    public void aplicarEscalamentoMalphion(GlobalBossMalphion boss) {


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


        evoluirMalphion(boss);
 	   
    }
    
    // ================== EVOLUÇÃO ==================
    public void evoluirMalphion(GlobalBossMalphion boss) {

        if (boss.getMaxHp() < 400_000L) {

        	boss.setName("MALPHION");
        	boss.setImageUrl("images/boss_evolution/boss_malphion/boss_malphion.webp");

        } else if (boss.getMaxHp() < 600_000L) {

        	boss.setName("MALPHION OVERLORD");
        	boss.setImageUrl("images/boss_evolution/boss_malphion/boss_malphion_overlord.webp");

        } else if (boss.getMaxHp() < 800_000L) {

        	boss.setName( "MALPHION OMEGA");
        	boss.setImageUrl("images/boss_evolution/boss_malphion/boss_malphion_omega.webp");

        } else {

        	boss.setName("MALPHION ETERNAL");
        	boss.setImageUrl("images/boss_evolution/boss_malphion/boss_malphion_eternal.webp");
        }
    }

   

    // Ataque direto (usado em auto-attack ou debug)
    public GlobalBossMalphion attack(long damage) {
    	GlobalBossMalphion boss = get();
        boss.applyDamage(damage);
        return repo.save(boss);
    }
}

